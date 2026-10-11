# TrainingTracker

![Kotlin](https://img.shields.io/badge/Kotlin-100%25-7F52FF.svg?logo=kotlin)
![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4.svg)
![Status](https://img.shields.io/badge/status-in%20development-yellow.svg)

An Android app for logging gym workouts — exercises, sets, reps, weight — built offline-first with a modern MVVM architecture. Started as a hands-on portfolio project to practice production-style Android patterns beyond tutorial-level apps.

## About

TrainingTracker lets you log workouts and see your training history, fully offline. Workouts are stored locally on the device, so the app works without an account, backend, or internet connection.

When signed in, workouts can be backed up to a personal cloud storage area and restored on another device. The app uses a Node.js/Express backend with PostgreSQL and JWT authentication.

## Features

**Done**
- [x] Browse a list of logged workouts
- [x] Live search/filter workouts by name
- [x] View workout details (exercises, sets, reps, weight)
- [x] Add a new workout through a validated form
- [x] Offline-first local persistence — data survives app restarts, no internet needed
- [x] Add / edit exercises within a workout
- [x] Edit and delete existing workouts
- [x] Workout dates
- [x] User accounts (register / login)
- [x] JWT-based authentication
- [x] Backup workouts to the backend
- [x] Restore workouts from the backend
- [x] Safe logout: back up unsaved workouts before clearing local data

**Planned**
- [ ] Workout statistics and progress charts
- [ ] Favorite exercises
- [ ] Push notification reminders

## Tech Stack

- **Kotlin**
- **Jetpack Compose** — UI toolkit
- **Navigation 3** — screen navigation (list → detail → create)
- **Hilt** — dependency injection
- **Room** — local database (SQLite)
- **Kotlin Coroutines & Flow** — async data and reactive state
- **Retrofit + OkHttp** — HTTP client for backend communication
- **Gson** — JSON serialization
- **Backend: Node.js, Express, PostgreSQL, JWT** — separate repository

## Architecture

Layered MVVM with a single source of truth:

```
UI (Compose)
↓ events
↑ state (StateFlow)

ViewModels
↓
Repositories
├── Room (SQLite) .......... local source of truth for the UI
└── Retrofit + AuthInterceptor
↓ HTTP + JWT
Backend: requireAuth → routes → PostgreSQL
```

A few deliberate decisions worth calling out:

- **Room is the source of truth for the UI.** Workouts are displayed from the local database, so the app remains usable offline.
- **Repository pattern** keeps a single place responsible for where data comes from. ViewModels never talk to Room or Retrofit directly.
- **Hilt** manages object creation and scoping (e.g. one shared repository instance app-wide) instead of manual dependency wiring.
- **Navigation 3** is used instead of the older Navigation Compose — it treats the back stack as plain observable state rather than a hidden graph, and is Google’s current recommended navigation library.
- **JWT authentication** identifies the user on every protected backend request. The backend derives `user_id` from the verified token, never from the request body.

## Known limitations

- **Backup/restore, not full sync.** Deletions are not propagated in either direction; restore brings back anything the cloud has that the device doesn’t.
- **This device always wins.** Backup overwrites the cloud copy and restore never touches existing workouts, so an edit made on a second device never reaches the first. No conflict resolution yet.
- **Token expiry is not handled.** An expired JWT results in a generic error instead of a prompt to sign in again.
- **Single-owner local database.** Logging out backs up and then wipes local data; there is no per-user partitioning on the device.
- **Possible duplicate on interrupted backup.** If the app is killed between creating a workout on the server and saving its remote id, the next backup creates a duplicate. Client-generated ids would make this idempotent.

## Screenshots

| Workouts | Add & Search | Offline |
|:---:|:---:|:---:|
| <img src="screenshots/EditButton.png" width="180px" alt="Screen of workouts" /> | <img src="screenshots/AddNewTrainingAndSearch.png" width="180px" alt="Add new training and search" /> | <img src="screenshots/RoomDataNoInternet.png" width="180px" alt="Room data without internet" /> |

## Getting Started

1. Clone the repository
2. Open in a recent version of Android Studio
3. Let Gradle sync
4. Run on an emulator or physical device

The app works locally without a backend. To use account login, backup, and restore, run the TrainingTracker backend separately.

## Project Structure

```
Workout.kt, Exercise.kt          — domain models (also Room entities)
WorkoutDao.kt, AppDatabase.kt    — Room persistence
Converters.kt                    — Room type converter (JSON for exercise lists)
WorkoutRepository.kt             — local workout data source
AuthRepository.kt                — login, logout, token and auth state
SyncRepository.kt                — backup and restore with the backend
AuthApi.kt, WorkoutApi.kt        — Retrofit API definitions
AuthInterceptor.kt               — adds the JWT to protected requests
TokenStorage.kt                  — stores the auth token locally
DatabaseModule.kt                — Hilt bindings
WorkoutListViewModel/Screen.kt   — workout list + search
WorkoutDetailViewModel/Screen.kt — workout detail view
WorkoutCreateViewModel/Screen.kt — add-workout form
SyncViewModel.kt                 — backup, restore and account actions
MainActivity.kt                  — navigation host
```

## Backend

The backend is a separate repository:

```text
Node.js
Express
PostgreSQL
JWT authentication
```

It provides user registration, login, protected workout routes, and per-user workout storage.

The Android app communicates with it through Retrofit, while `AuthInterceptor` attaches the JWT token to authorized requests.

## Backend README

U backend repozitoriju napravi `README.md` s ovim sadržajem:

# TrainingTracker Backend

Node.js/Express API for the TrainingTracker Android app: user accounts (bcrypt + JWT) and per-user workout storage in PostgreSQL.

## Setup

1. Install dependencies:

```bash
npm install
```

2. Copy `.env.example` to `.env` and fill in your values:

```bash
cp .env.example .env
```

Example:

```env
JWT_SECRET=change-me
DATABASE_URL=postgresql://user:password@localhost:5432/trainingtracker
```

3. Create the database tables using the schema below.

4. Start the server:

```bash
node index.js
```

The API listens on port `3000`.

## Database schema

```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email TEXT UNIQUE NOT NULL,
    password_hash TEXT NOT NULL
);

CREATE TABLE workouts (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id),
    name TEXT NOT NULL,
    date_millis BIGINT NOT NULL,
    exercises JSONB NOT NULL DEFAULT '[]'
);
```

## API

| Method | Path | Auth | Description |
|---|---|---|---|
| POST | `/api/register` | no | Create an account |
| POST | `/api/login` | no | Log in and receive a JWT |
| GET | `/api/me` | JWT | Get the current user’s id |
| GET | `/api/workouts` | JWT | List the current user’s workouts |
| POST | `/api/workouts` | JWT | Create a workout |
| PUT | `/api/workouts/:id` | JWT | Update one of the user’s own workouts |

## Design notes

- `user_id` always comes from the verified JWT (`req.userId`), never from the request body.
- All SQL queries use parameterized queries.
- Ownership is checked in the same `WHERE` clause as the workout id:

```sql
WHERE id = $1 AND user_id = $2
```

- Passwords are hashed with bcrypt.
- JWTs are used to authenticate protected requests.