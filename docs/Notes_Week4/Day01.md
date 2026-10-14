# Tjedan 4, Dan 1 – Plan i odluke: što gradim do kraja

## Zadatak: pet scenarija

Za svaki scenarij napiši koje retke u kojim tablicama stvaraš ili mijenjaš.

```text
workout_session = jedan trening
id: session-1
startedAt: 2026-10-14T16:00:00Z
timezoneId: Europe/Zagreb
endedAt: null
createdAt: 2026-10-14T16:00:00Z
updatedAt: 2026-10-14T16:00:00Z
deletedAt: null

exercise = vježba u katalogu
id: bench-press-uuid
name: Bench Press
isCustom: false

logged_set = jedan stvarno odrađeni set
id
sessionId
exerciseId: bench-press-uuid
position
reps
weightKg
createdAt
updatedAt
deletedAt
```

## 1. Upiši običan trening: dvije vježbe, po tri seta

Scenarij: korisnik radi bench press i row, po tri seta.

Stvara:

```text
1 workout_session
id: session-1
startedAt: 2026-10-14T16:00:00Z
timezoneId: Europe/Zagreb
endedAt: null
createdAt: 2026-10-14T16:00:00Z
updatedAt: 2026-10-14T16:00:00Z
deletedAt: null
```

```text
6 logged_set redaka
id: set-1, 2, 3
sessionId: session-1
exerciseId: bench-press
position: 0, 1, 2
reps: 8
weightKg: 60
```

```text
logged_set
id: set-4, 5, 6
sessionId: session-1
exerciseId: row
position: 3, 4, 5
reps: 10
weightKg: 50
```

`position` predstavlja redni broj seta unutar cijelog treninga, kako bi se kasnije mogli dodavati supersetovi naizmjeničnih više vježbi.

Na kraju:

postavi `endedAt` na sesiji:

```text
workout_session
id: session-1
endedAt: 2026-10-14T17:05:00Z
updatedAt: 2026-10-14T17:05:00Z
```

## 2. Uredi jedan set tri dana kasnije

Mijenja se samo jedan redak `logged_set`.

Prije:

```text
id: set-3
sessionId: session-1
exerciseId: bench-press
position: 2
reps: 8
weightKg: 60
updatedAt: 2026-10-14T16:20:00Z
```

Poslije:

```text
id: set-3
sessionId: session-1
exerciseId: bench-press
position: 2
reps: 8
weightKg: 62.5
updatedAt: 2026-10-17T18:00:00Z
```

`workout_session` se ne mijenja, osim možda `updatedAt`.

## 3. Usred treninga zamijeni vježbu drugom

Stari setovi zadržavaju staru vježbu, novi setovi dobivaju novu vježbu, a `position` raste kroz cijelu sesiju.

## 4. Počni trening i napusti ga nakon dvije vježbe

Sesija bez `endedAt`, s dva spremljena seta.

Ako postoje `logged_set` retci → računaj ih.  
Ako nema → ne računaj ništa.

Svaki upisani set mora se odmah spremiti u Room. I tako korisnik kad se vrati imat će poruku “Imaš nezavršenih treninga od 14. listopada”, “Nastavi trening?”

## 5. Prebaci prikaz na lb i pogledaj staru povijest

Aplikacija uzme `weightKg` i pretvori ga samo za ekran.  
Baza se ne mijenja. Samo se prikaz pretvara iz kg u lb.

# Audit dizajna prema rubrici

## Lista treninga

- Škiljenje: djelomično; previše akcija u top baru
- Grayscale: ne; ručna LightGray boja
- Primarna radnja: da, FAB Dodaj trening
- Razmaci: uglavnom 16 dp; provjeriti dupli padding
- Fontovi: 3-4; provjeriti
- Boje: previše izvora; maknuti ručne boje
- Jedna ruka / loše svjetlo: FAB dobar, gornje ikone lošije
- Brojke: nisu ključne ovdje

## Detalji treninga

- Škiljenje: ne; brojke premale
- Grayscale: provjeriti kontrast
- Primarna radnja: nejasno između Uredi i Obriši
- Razmaci: provjeriti
- Fontovi: previše hijerarhije
- Boje: koristiti temu
- Jedna ruka: akcije su gore
- Brojke: moraju biti veće i poravnate

## Forma vježbe

- Škiljenje: djelomično; previše polja
- Grayscale: provjeriti greške
- Primarna radnja: Spremi
- Razmaci: standardizirati
- Fontovi: u redu
- Boje: koristiti theme.error
- Jedna ruka: previše skrolanja
- Brojke: povećati inpute

# Početna mjera: broj dodira

1. Klik na FAB
2. Upis imena
3. Klik Spremi
4. Otvori trening
5. Klik FAB za vježbu
6. Upis vježbe
7. Upis serija
8. Upis ponavljanja
9. Upis kilaže
10. Klik Spremi

Trenutačno 10 dodira.