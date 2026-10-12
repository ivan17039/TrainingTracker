# Tjedan 3, Dan 7 – Detaljni plan: Zatvaranje tjedna

## Kako aplikacija radi?

### Lice aplikacije - Ekran

Predstavlja samo ono što korisnik vidi: lista treninga, gumb za dodavanje, ikone za prijavu, oblak, backup.  
Ne zna odakle podaci stvarno dolaze, ne pokušava sam riješiti problem, već prosljeđuje interakcije korisnika i prikazuje rezultate.

### Mozak ekrana - ViewModel

Sloj koji stoji odmah iza ekrana tako što prima njegove događaje i odlučuje što treba napraviti.  
Ne obavlja funkcionalnosti već govori repository-u koju točno operaciju da izvrši (spremanje, slanje u bazu).  
Sadrži stanja koje ekran treba prikazati: listu treninga, je li korisnik prijavljen, je li sinkronizacija u tijeku, poruka o poslanim treninzima.

### Skladištar podataka - Repository

Jedini koji zna odakle i kamo podaci idu.  
Smije koristiti Room i Retrofit kako bi znao odakle podatke dohvatit, spremiti, poslati.  
Temeljem što ViewModel kaže Repository odluči kako obaviti cjelokupnu operaciju.

### Lokalna baza na telefonu - Room

Baza podataka koja živi na telefonu.  
Dodavanje novih podataka prvotno se sprema na Room.  
Radi i offline te prilikom spajanja na mrežu korisnik može pristupiti backup-u i poslati na server.  
Ekran prikazuje ono što je u Room-u, a ne ono što je direktno došlo s backup-a.  
Proces ide tako kad se Room promjeni, obavijesti se ViewModel koji ažurira ekran, s time je nova lista odmah vidljiva.

### Dostavljač prema backendu - Retrofit

Alat koji Android aplikacija koristi da razgovara s backendom preko interneta.  
Šalje zahtjeve i prima odgovore.  
Prenosi podatke ali ne odlučuje što spremiti u bazu podataka.

### Centralno skladište - Backend

Backend je server koji živi negdje drugdje, općenito na računali ili oblaku.  
Ima vlastitu bazu podataka.  
Obavljanje operacije backup trening se sprema na bazu, a restore vraća podatke iz baze (npr kod prijave s drugog uređaja).

Backend pri prijavi:

- korisnik pošalje email i lozinku
- backend provjeri jesu li ispravni
- ako jesu backend izda JWT token
- aplikacija spremi taj token
- kod svakog idućeg zahtjeva aplikacija šalje token

## Proces dodavanja treninga:

Klik na dodavanje treninga - ispunjavanje forme i klik na spremi - (događaj ide na ViewModel) - provjera podataka i poziv repository-a (spremanje) - repository pozove Room i sprema trening u lokalnu bazu - room dodijeli treningu lokalni ID - nakon što se trening spremi ViewModel dobije novu listu koju ekran odmah prikaže

Kasnije korisnik može pritisnuti backup gdje aplikacija uzme treninge iz Room-a i pošalje backendu.

`remoteId` je ID koji je treningu dao backend. Ako postoji, znaš da je trening već poslan na server.

## Backup:

Ekran javlja ViewModelu: “Backup.”  
ViewModel javlja repositoryju: “Pošalji treninge.”  
Repository uzme treninge iz Rooma.  
Repository ih pošalje backendu kroz Retrofit.  
Interceptor doda token.  
Backend provjeri token i spremi treninge.  
Repository spremi remoteId u Room.  
ViewModel prikaže poruku.

## Odjava:

1. Prvo pošalji sve nespremljene treninge na backend.
2. Tek onda obriši lokalne treninge.
3. Tek na kraju obriši token.

Odjava prvo radi backup, a tek onda briše lokalne podatke i token.

## Vježbe

### 1. Napiši treći test: Workout() bez argumenata osim obaveznih ima id == 0, remoteId == null i praznu listu vježbi.

```kotlin
@Test
fun workout_hasDefaultValues() {
    val workout = Workout(
        name = "Leg Day",
        dateMillis = 1_700_000_000_000L
    )

    assertEquals(0, workout.id)
    assertEquals(null, workout.remoteId)
    assertEquals(0, workout.exercises.size)
}
```

### 2. (Bez koda) Koji bi sloj sljedeći testirao i zašto? Što taj sloj čini težim za testiranje od mappera?

Testirao bi restore logiku pullWorkouts() odlučivanje koji su backend treninzi novi za uređaj.  
Složeni je sloj jer miješa retorfit poziv, čitanje i upis Room, pretvorbu RemoteWorkout u Workout i logiku filtriranja treninga.

### 3. (Stretch) U pullWorkouts() izdvoji čisti dio ("koje od ovih iz oblaka su nove za ovaj uređaj") u zasebnu funkciju findNewWorkouts(remote, local) koja nema mrežu ni Room, samo liste. Napiši joj test. To je cijela tajna testabilnog koda: čista logika odvojena od ulaza i izlaza.

```kotlin
fun findNewWorkouts(
    remote: List<RemoteWorkout>,
    local: List<Workout>
): List<RemoteWorkout> {
    val knownRemoteIds = local
        .mapNotNull { it.remoteId }
        .toSet()

    return remote.filter { remoteWorkout ->
        remoteWorkout.id !in knownRemoteIds
    }
}
```

### 4. (Stretch) Napravi nove screenshotove (npr. prijavljen s ikonama oblaka) i zamijeni stare u README-u.