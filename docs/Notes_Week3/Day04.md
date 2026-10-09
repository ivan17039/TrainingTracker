# Tjedan 3, Dan 4 – Detaljni plan: Prepoznavanje već poslanog

Trenutno `pushAllWorkouts()` uvijek pravi novi red i svaki put na sync dodaju se vježbe koje su već dodane.

Potrebno je dodati u Room polje koje pamti odgovor.

## Rješenje

1. `Workout` dobiva novo polje `remoteId` - pod kojim brojem to postoji na backendu.

2. Backend dobiva `PUT /api/workouts/:id` - ruta koja ažurira umjesto `POST` koji radi novi.

3. Sync logika se grana: na početku `remoteId` je `null`, onda se obavlja `POST` i backend vrati ID koji se spremi pod `remoteId`, ako postoji već onda `PUT` na taj id.

```kotlin
if (workout.remoteId == null) {
    // Trening nikad nije poslan na backend.
    // Napravi novi red na backendu.
    // POST /api/workouts

} else {
    // Trening već postoji na backendu.
    // Nemoj raditi novi red.
    // Ažuriraj postojeći red.
    // PUT /api/workouts/{remoteId}
}
```

## URL parametri s `:id`

Do sad je sve bilo fiksno, sada kad se radi o posebnim treninzima uvodi se ruta koja je drugačija u osnovi na koji je trening u pitanju.

```text
/api/workouts/:id
```

Što god stoji pod `:id`, Express prepozna kao placeholder preko `req.params.id`.

Isto i za Retrofit:

```kotlin
@PUT("api/workouts/{id}")
suspend fun updateWorkout(@Path("id") id: Int, @Body request: CreateWorkoutRequest): RemoteWorkout
```

## Dodavanje PUT rute unutar backenda

Sad nakon `PUT http://localhost:3000/api/workouts/3` i postavljanja drugog imena treninga (iz `Walking` u `Push Day`) u body dobijem:

```json
[{"idx":2,"id":3,"user_id":7,"name":"Push Day","date_millis":1790948183841,"exercises":"[]"}]
```

***

# Vježbe

## 1. PUT na tuđi trening

Pokušaj `PUT /api/workouts/<id tuđeg treninga>` (iz drugog korisnika, Dan 2 vježba) sa SVOJIM tokenom. Potvrdi 404, ne 200 niti 500.

```json
{
  "error": "Trening nije pronađen"
}
```

***

## 2. Logiranje POST i PUT poziva

U `SyncRepository`, dodaj privremeni `println` ili `Log` koji ispiše je li za svaki trening pozvan `POST` ili `PUT` — pokreni sync dvaput zaredom, potvrdi da se prvi put za SVE ispiše "POST", drugi put za SVE "PUT".

### Prvi sync – POST

```text
2026-10-09 14:19:18.263 11076-11076 System.out  com.ivanb.trainingtracker  I  POST Push Day
2026-10-09 14:19:18.352 11076-11076 System.out  com.ivanb.trainingtracker  I  POST Pull Day
2026-10-09 14:19:18.407 11076-11076 System.out  com.ivanb.trainingtracker  I  POST Leg Day
2026-10-09 14:19:18.458 11076-11076 System.out  com.ivanb.trainingtracker  I  POST Rest Day
```

### Drugi sync – PUT

```text
2026-10-09 14:20:49.276  6298-6298  System.out  com.ivanb.trainingtracker  I  PUT Push Day (remoteId=1)
2026-10-09 14:20:49.596  6298-6298  System.out  com.ivanb.trainingtracker  I  PUT Pull Day (remoteId=2)
2026-10-09 14:20:49.652  6298-6298  System.out  com.ivanb.trainingtracker  I  PUT Leg Day (remoteId=3)
2026-10-09 14:20:49.704  6298-6298  System.out  com.ivanb.trainingtracker  I  PUT Rest Day (remoteId=4)
```

***

## 3. Server pukne između `createWorkout` i `setRemoteId`

**(Bez koda)** Što bi se dogodilo da je backend server pukao TOČNO između `createWorkout` poziva i `setRemoteId` poziva u Koraku 5 — koje je stanje tad lokalni trening, i što bi se dogodilo pri idućem sync-u?

Lokalni trening bi u toj situaciji ostao `remoteId = null`, tijekom sljedećeg sync-a ponovno bi poslao `POST` i backend bi mogao stvoriti duplikat.

Tek kad se `remoteId` uspješno spremi u Room sljedeći sync koristi `PUT` i ažurira postojeći trening.

***

## 4. Stretch – sinkronizacija brisanja

**(Stretch)** Trenutno, obrišeš li lokalni trening koji VEĆ ima `remoteId`, njegova kopija na backendu ostaje zauvijek — brisanje se ne sinkronizira. Razmisli (ne moraš pisati kod) što bi trebalo dodati da se i brisanje prenese na backend.

Trebao bi dodati `DELETE` zahtjev u `WorkoutApi`.

Potrebno je dodatno označiti izbrisani trening `isDeleted = true`, ne odmah fizički ukloniti iz Room-a.

Dakle pri sljedećem syncu aplikacija pronađe lokalno označeni `remoteId` trening, pošalje `DELETE` na taj trening i tek onda kad backend potvrdi brisanje, aplikacija ukloni taj trening iz Rooma.