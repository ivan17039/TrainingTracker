# Tjedan 1, Dan 2 – Detaljni plan: ViewModel, StateFlow (Android)

**ViewModel** – dizajniran kako bi promjena konfiguracije ekrana ostala ista prilikom rotacije ekrana:

* Vezan je za Activity/lifecycle scope, a ne za pojedini Composable.

Ne koristimo `remember` koji čak preživi rekompoziciju, ali ne preživljava da se cijeli Activity uništi i ponovno napravi (rotacija ekrana upravo to radi).

* **`remember { mutableStateOf(...) }`** – sačuva state koji pokreće rekompoziciju kad se promijeni.
* **`rememberSaveable`** – isto kao `remember` te dodatno preživi rotaciju (sprema u Bundle), samo što ne daje mjesto za pravu poslovnu logiku (filtriranje, testiranje).
* **ViewModel + StateFlow** – uvedeno za state koji predstavlja stvarni podatak aplikacije (lista treninga, trenutni search upit) = preživi rotaciju, odvojen UI.

**Ključni pojmovi:**

* **`MutableStateFlow`**: privatna verzija koju samo ViewModel može mijenjati.
* **`StateFlow`**: javna read-only verzija koju Composable čita.
* **`.asStateFlow()`**: pretvara Mutable verziju u read-only za vanjski svijet.
* **`collectAsState()`**: uzima tok podataka iz ViewModela (`StateFlow`) i pretvara ga u oblik koji Compose razumije kako bi se ekran osvježio na promjenu podataka.

---

## Vježbe

### 1. Testiranje prazne liste

Testiraj: upiši nešto što ne postoji (npr. "xyz"). Lista postane prazna, ali ekran ne prikazuje nikakvu poruku – samo prazan prostor.

Dodaj u `WorkoutListScreen` provjeru `if (workouts.isEmpty())` koja prikaže `Text("Nema rezultata za \"$searchQuery\"")` umjesto `LazyColumn`-a.
<img src="./screenshots/EmptyListMessage.png" width="250" alt="Prazna lista poruka">
```kotlin
if (workouts.isEmpty()) {
    Text(
        text = "Nema rezultata za \"$searchQuery\"",
        modifier = Modifier.padding(16.dp)
    )
} else {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(workouts) { workout ->
            WorkoutCard(workout = workout)
        }
    }
}



```

---

### 2. Dodavanje ikone za brisanje (trailingIcon)

Dodaj `trailingIcon` na `TextField` (npr. malo "x") koji poziva `viewModel.onSearchQueryChange("")` i briše upit.
<img src="./screenshots/TrailingIcon.png" width="250" alt="Ikona za brisanje unesenog teksta">
```kotlin
trailingIcon = {
    // Prikaži gumb za brisanje samo ako ima unesenog teksta
    if (searchQuery.isNotEmpty()) {
        IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
            Icon(
                imageVector = Icons.Default.Clear, // Ikona iksa ("x")
                contentDescription = "Očisti pretragu"
            )
        }
    }
}

```

---

### 3. Zašto privatne varijable imaju `_` prefiks?

(Bez koda, razmisli) Zašto `_searchQuery` i `_workouts` imaju `private` + `_` prefiks, dok `searchQuery` i `workouts` (bez `_`) su javni? Kako se taj pattern zove i zašto ga koristimo umjesto da sve bude javno i mutable?

Uzorak sa skrivenim poljima (**Backing Property Pattern**).

Imamo 2 verzije iste varijable radi sigurnosti zaštite podataka i arhitekture.

Privatne varijable s prefiksom `_` služe ViewModelu za izmjenu stanja, dok javne varijable služe UI-ju samo za čitanje stanja bez mogućih izmjena.

---

### 4. Ponašanje prilikom rotacije ekrana

Rotiraj ekran dok je search upisan (ponovi iz Bloka 3, ali sad svjesno objasni zašto). Zapiši svojim riječima: da smo query držali u `remember { mutableStateOf("") }` unutar `WorkoutListScreen`-a umjesto u ViewModelu, što bi se točno dogodilo nakon rotacije?

Kad se ekran rotira, Androidov OS potpuno uništi i stvori Activity i sve Composable funkcije na ekranu kako bi se ažurirao novi izgled ekrana.

Da je korišten `remember { mutableStateOf("") }`, tekst unesen u tražilicu bi nakon rotacije bio izbrisan. `remember` živi samo dokle Composable živi u memoriji.

ViewModel je dizajniran da preživljava promjene konfiguracije (rotacije), pa su stanja kod ViewModela sačuvana u `_searchQuery`.