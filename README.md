# AuraFit — Android

Android client for **AuraFit**, an AI-powered virtual try-on app. Pick a photo of yourself and a garment photo, and AuraFit generates a composite image showing the garment on you via the PixelCut AI try-on API. This app is the Android sibling of the AuraFit web app.

## Tech Stack

- Kotlin, single-Activity app with Jetpack Compose UI (no XML layouts)
- Architecture: lightweight MVVM — `TryOnViewModel` (StateFlow) + Compose UI + repository/provider interfaces, split into `domain` / `data` / `di` / `ui` packages
- DI: [Koin](https://insert-koin.io/)
- Networking: [Ktor](https://ktor.io/) (OkHttp engine, kotlinx-serialization)
- Image loading: [Coil](https://coil-kt.github.io/coil/)
- [Firebase](https://firebase.google.com/) (Analytics, Storage)

**SDK versions:** compileSdk 36, minSdk 24, targetSdk 36 · AGP 8.12.3 · Kotlin 2.0.21

## Project Structure

Single-module app (`:app`). Source under `app/src/main/java/com/aurafit/app/`:

```
AuraFitApp.kt              # Application class, Koin startup
MainActivity.kt
data/
  network/                  # KtorClientFactory, FirebaseUploader
  providers/                # PixelCutProvider
  repository/                # PixelCutRepository
di/
  Modules.kt                 # networkModule, repoModule, viewModelModule
domain/
  TryOnContracts.kt           # TryOnProvider interface, DTOs
ui/
  theme/
  tryon/                      # TryOnScreen (Compose), TryOnViewModel
util/
  PermissionHelper.kt
```

## Getting Started

1. Open the project root in Android Studio and let Gradle sync (wrapper included: `gradlew` / `gradlew.bat`).
2. `app/google-services.json` (Firebase config) is already committed, so no extra setup is needed for Firebase.
3. Run the `app` configuration on a device or emulator (minSdk 24+).

## Configuration

The PixelCut API base URL and key are wired in as `buildConfigField`s from `gradle.properties` (debug build type only — release has no provider configured yet):

```properties
PIXELCUT_URL=...
PIXELCUT_API_KEY=...
```

> **Security note:** `gradle.properties` currently has a real-looking API key committed in plaintext. Rotate it and move it to a non-committed local file (e.g. `local.properties`, gitignored) before treating this repo as public.

## Testing

```bash
./gradlew test               # unit tests
./gradlew connectedAndroidTest # instrumented tests (needs a device/emulator)
```

Only placeholder tests exist today (`ExampleUnitTest`, `ExampleInstrumentedTest`) — real coverage is still to be written.

## CI/CD

None configured yet.
