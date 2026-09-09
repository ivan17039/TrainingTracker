# Tjedan 1, Dan 4 – Repository + Hilt

## Repository

Repository je klasa koja jedina zna gdje i kako dohvaća podatke. [^1]

Trenutno je u projektu `DummyData` na dva mjesta: `WorkoutListViewModel` i `WorkoutDetailScreen`.  
Kasnije, kada zamijenimo te podatke s pravim podacima (Room), lakše će se dohvaćati podaci ako imamo odvojeni `WorkoutRepository`. [^1]

---

## Hilt

Hilt je alat koji stvara objekte (npr. Repository) i ubacuje ih tamo gdje trebaju biti – to se zove **Dependency Injection (DI)**. [^2]

Bez Hilta, svaki ViewModel bi morao sam stvarati `WorkoutRepository`. S Hiltom:

- Hilt sam vidi što ViewModel treba
- to stvori
- i preda ViewModelu. [^2]

```kotlin
@HiltViewModel
class WorkoutListViewModel @Inject constructor(
    private val repository: WorkoutRepository
) : ViewModel() {
    // Hilt sam stvori repository i preda ga ovdje
}
```

---

## Hilt adnotacije

| Adnotacija           | Ide na                | Znači                                                                 |
|----------------------|-----------------------|-----------------------------------------------------------------------|
| `@HiltAndroidApp`    | `Application` klasu   | "Ovdje Hilt počinje raditi" – pokreće DI sustav za cijelu aplikaciju. [^2] |
| `@AndroidEntryPoint` | `MainActivity`        | "Ovaj ekran smije tražiti stvari od Hilta" – omogućuje injection u Activity. [^2] |
| `@HiltViewModel`     | `ViewModel` klasu     | "Hilt smije napraviti ovaj ViewModel" – označava ViewModel za DI. [^2] |
| `@Inject constructor(...)` | Konstruktor     | "Ovo treba dati ovoj klasi" – definira dependencyje koje Hilt treba injektirati. [^2] |

---

## Vježbe

### 1. Dodavanje nove funkcije u Repository

Otvori `WorkoutRepository.kt` i dodaj treću funkciju:

```kotlin
fun getWorkoutCount(): Int = DummyData.workouts.size
```

Ne moraš je nigdje koristiti danas – samo je napiši i provjeri da se projekt i dalje gradi.

**Rezultat:** Dodao sam `fun getWorkoutCount(): Int = DummyData.workouts.size` i sve se dobro gradi.

---

### 2. Eksperiment s `@Inject` na ViewModelu

U `WorkoutListViewModel`, privremeno obriši samo riječ `@Inject` s konstruktora i pokreni app. Pogledaj grešku koju Hilt baci – pročitaj je, ne moraš je razumjeti do kraja. Vrati `@Inject` natrag.

**Greška:**

```text
[Hilt] @HiltViewModel annotated class should contain exactly one @Inject or @AssistedInject annotated constructor.
[Hilt] Processing did not complete. See error above for details.
```

**Zaključak:** Greška govori da ako sam na ViewModel stavio `@HiltViewModel`, onda ViewModel mora imati točno jedan konstruktor označen s `@Inject`. [^2]

---

### 3. (Bez koda) Razlika između dva `@Inject` konstruktora

**Pitanje:** `WorkoutRepository` ima prazan `@Inject constructor()`. `WorkoutListViewModel` ima `@Inject constructor(private val repository: WorkoutRepository)`. Što je razlika između ta dva?

**Odgovor:**

`WorkoutRepository` ne treba nikakve parametre da bi se stvorio. Stoga prazan `@Inject` označava: "Kad netko zatreba `WorkoutRepository`, možeš ga sam stvoriti i ne treba mi ništa izvana." [^2]

Trenutno, dok još sadrži `DummyData`, ne ovisi o ničemu drugom.

`WorkoutListViewModel` treba parametar `WorkoutRepository` i tu `@Inject` označava da za stvaranje `WorkoutListViewModel`-a prvo se stvori `WorkoutRepository` i preda u ovaj konstruktor. [^2]

**Ukratko:** `WorkoutRepository` Hilt može sam stvoriti jer ne treba dependencyje, dok `WorkoutListViewModel` treba već stvoren `WorkoutRepository` da bi se mogao stvoriti. [^2]

---

[^1]: Repository pattern – skriva detalje o izvoru podataka iza jedne klase.
[^2]: Hilt – Googleov DI framework za Android, automatizira stvaranje i injektiranje dependencyja.