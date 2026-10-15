# Tjedan 4, Dan 2 – Detaljni plan: Dizajn sustav i prvi dio modela v2

## Obavljen dizajn sustava

- Seed boja iz Theme Buildera, tema se zove TrainingTrackerTheme, dynamic color isključen
- Spacing.kt postoji, svi fileovi sada koriste samo Spacing i teme umjesto '.dp)'
- Nigdje izvan ui/theme/ nema Color.
- Tamna tema pregledana na sva četiri ekrana

## Dodane tri tablice uz postojeću workouts.

String primarni ključ (UUID).  
Do sad: `@PrimaryKey(autoGenerate = true) val id: Int`. Sad: `@PrimaryKey val id: String = UUID.randomUUID().toString()`.  
Id nastaje u aplikaciji, ne u bazi, pa ga znaš prije spremanja i isti je svugdje.

## @Relation i @Transaction

Do sad su vježbe bile JSON unutar treninga.  
Sad su pravi redci u drugoj tablici, pa treba način da "trening i njegove setove" dobijemo zajedno.  
Napravi se klasa s `@Embedded` (roditelj) i `@Relation` (djeca); Room pod haubom pokrene dva upita (sesije, pa setove).  
`@Transaction` ih veže u jednu cjelinu.