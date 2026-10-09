# Tjedan 3, Dan 5 – Detaljni plan: Vraćanje iz oblaka (restore)

Restore - vrati sve iz oblaka što trenutna aplikacija nema
- opcija lokalnog vraćanja obrisanih treninga

## Kako restore prepozna koji su treninzi na uređaju?

```text
oblak ima treninge s id:      12, 13, 14
ovaj uređaj ima remoteId:     12, null
poznati remoteId-evi:         {12}
novo za ovaj uređaj:          13, 14
```

null - novo na uređaju još ne proslijeđeno oblaku

## Vježbe

### 1. (Predvidi, pa testiraj)

Obriši lokalno trening koji već ima remoteId, pa pritisni Restore. Što se dogodi? Zašto je to ispravno ponašanje za restore?

Nakon što sam obrisao i pritisnuo Restore taj trening se vratio.
Cilj restore-a je dohvatiti sve što oblak ima a trenutni ekran nema.
Restore dohvati treninge s backenda i zadrži samo one čiji id nije u poznatim remoteId-evima.
Kada korisnik obriše trening lokalno on i dalje postoji na backend-u što je i cilj restora, ne zna se jeli slučajno izbrisan ili nije uopće postojao.

### 2. (Predvidi, pa testiraj)

Drugi korisnik na istom uređaju: registriraj ga kroz Postman (POST /api/register), u appu se prijavi kao on, pa pritisni Backup. Ako su svi tvoji lokalni treninzi već poslani (imaju remoteId), što očekuješ? Pročitaj Blok 7 točku 2 nakon što probaš.

Taj novi korisnik će imati treninge od korisnika prije odnosno onoga kojem zapravo pripadaju.
Room ne pamti kojem korisniku pripada koji trening i tko god se prijavi naslijedi sve što je tu.

### 3. (Stretch)

Napravi fun Workout.toRequest(): CreateWorkoutRequest kao zrcalnu sliku toWorkout(), i iskoristi je u pushAllWorkouts() umjesto ručnog građenja zahtjeva.

Napravio sam zrcalni mapper za backup:

```kotlin
fun Workout.toRequest(): CreateWorkoutRequest {
    return CreateWorkoutRequest(
        name = name,
        dateMillis = dateMillis,
        exercises = exercises
    )
}
```
Umjesto da u `pushAllWorkouts()` ručno gradim `CreateWorkoutRequest` za svaki trening, sada zovem:

```kotlin
val request = workout.toRequest()
```

### 4. (Radoznalost)

U Postmanu pogledaj odgovor GET /api/workouts: je li date_millis u navodnicima? Ako jest, kako to da ga Gson svejedno pročita kao Long?

```text
JSON:  "1791560548103"  (String)
Kotlin: 1791560548103   (Long)
```

Da pod navodnicima je "date_millis": "1791560548103".
Gson ga može pretvoriti u Kotlinov Long jer je svojstvo u podatkovnoj klasi deklarirano kao Long:

```kotlin
data class RemoteWorkout(
    @SerializedName("date_millis")
    val dateMillis: Long
)
```

Ovo je potrebno radi preciznosti kod velikih brojeva koje JavaScript ne može pročitati kao običan Number.
