# Tjedan 1, Dan 1 – Detaljni plan: Training Tracker

**Training Tracker zahtjeva:** auth, kreiranje treninga, vježbe, serije, ponavljanja, kilaža, povijest, statistika, offline pristup, sinkronizacija

* **Composable funkcija** - obična funkcija označena s `@Composable` koja opisuje kako UI treba izgledati (deklerativni UI - "ovdje ide to i to", composable odluči kako i kad to nacrtati)
```kotlin
@Composable
fun Pozdrav(ime: String) {
    Text(text = "Bok, $ime!")
}

```


* **Recomposition** - ponovni poziv (redraw) one funkcije koje su čitale state koji se promijenio
* **Modifier** - objekt kojim composable-u mijenjamo izgled
* **LazyColumn** - prikazuje listu ali samo renderira ono što je vidljivo na ekranu

---

## Vježbe

### 1. Dodaj četvrti dummy trening u `DummyData.kt` (npr. "Rest Day" bez vježbi) — pokreni app, provjeri da se pojavi u listi bez ijedne druge promjene koda.

```kotlin
Workout(
    id = 4,
    name = "Rest Day",
    date = "01.09.2026.",
    exercises = listOf()
)

```

---

### 2. U `WorkoutCard`, dodaj `Icon` composable (npr. `Icons.Default.FitnessCenter`) pored imena treninga, unutar `Row`-a.

```kotlin
Row(verticalAlignment = Alignment.CenterVertically) {
    Text(text = workout.name, style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.width(8.dp))
    Icon(
        imageVector = Icons.Default.FitnessCenter,
        contentDescription = "Trening ikona"
    )
}

```

---

### 3. Zamijeni `Modifier.padding(horizontal = 16.dp, vertical = 8.dp)` u `WorkoutCard`-u s `Modifier.background(Color.LightGray).padding(16.dp)`, pa onda obrnutim redoslijedom (padding pa background) — usporedi vizualnu razliku i zapiši svojim riječima zašto se razlikuju.

<img src="./screenshots/ModifierOrderDifferences.png" width="250" alt="Razlike u redoslijedu kod modifier-a">

* **Prije `background()`:** Djeluje kao vanjski razmak (margin). Imamo smanjeni prostor za crtanje jer se prvo kartica odmakne pa se pozadina postavlja.
* **Poslije `background()`:** Djeluje kao unutarnji razmak (padding). Imamo sivu pozadinu i onda se unutar toga kartica odmakne.

---

### 4. Bez gledanja u kod: zašto `WorkoutListScreen` koristi `LazyColumn`, a ne obični `Column` s `forEach` petljom? Što bi se dogodilo na performansama kad bi trening lista narasla na 500 stavki?

Koristimo `LazyColumn` zbog situacije kada učitavamo više stotina ili tisuća vježbi. Kod `LazyColumn`-a na ekran se renderira onoliko vježbi koliki je ekran i dodatna 1 zbog skrolanja.