# Tjedan 4, Dan 3 – Detaljni plan: Repozitorij nad novim modelom

Repositroy predstavlja sloj između UI-a i baze podatka.

```text
UI → Repository → DAO → Room baza
```

Ekran ne zove direktno `SessionDao` za pristup podatcima već preko Repository-a odlučuje što treba napraviti u bazi.

## Pet poslova repositorija

- `startOrResume()`: započni ili nastavi trening

1. Traži nezavršenu sesiju.
2. Ako postoji, vrati je.
3. Ako ne postoji, stvori novu.

- `logSet(...)`: upiši set, repository sam izračuna position

```text
Prazna sesija
MAX(position) = NULL
COALESCE(NULL + 1, 0) = 0
→ prvi set dobiva position 0
```

- `lastSetFor(exerciseId)`: što je bilo zadnji put

```text
Prošli trening: Bench Press, 8 x 60 kg
Danas otvoriš Bench Press
→ aplikacija predloži 8 i 60
```

- `endSession(id)`: postavlja endedAt za završeni trening

- `seedCatalogIfEmpty()`: ubacuje zadane vježbe samo jednom

```text
Ako katalog već ima vježbe, ne diraj ništa.
Ako je prazan, ubaci zadane vježbe.
```