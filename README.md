# Geheimzentrale

This repository contains the source code for an Android application built with Jetpack Compose and Hilt.
The app provides an offline catalogue of "Die drei ???" episodes.
It reads the episode data from `app/src/main/assets/episodes.json` so no network connection is required while using the app.

## Prerequisites

- **Android Studio** with the Android SDK installed
- **JDK 11** (installed with Android Studio or separately)

## Building the app

Run the following command from the repository root to build a debug APK:

```bash
./gradlew assembleDebug
```

The generated APK can be found in `app/build/outputs/apk/debug/`.

## Running tests

Unit tests can be executed with:

```bash
./gradlew test
```

Instrumented tests can be started on a connected device with `./gradlew connectedAndroidTest`.


## License

This project is licensed under the [Apache License 2.0](LICENSE).
