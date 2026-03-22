# CocktailApp (Kotlin + Android Studio)

Aplikacja spełnia wymagania:
- lista koktajli (zdjęcie + tytuł),
- ekran szczegółów wybranego drinka,
- wyszukiwanie drinków po nazwie.

## Stack
- Kotlin
- Jetpack Compose
- Retrofit + Kotlinx Serialization
- Coil
- MVVM

## API
Aplikacja korzysta z publicznego API:
- `GET /api/v1/cocktails`
- `GET /api/v1/cocktails/{id}`

Base URL:
- `https://cocktails.solvro.pl/`

## Jak uruchomić
1. Otwórz folder projektu w Android Studio.
2. Poczekaj na synchronizację Gradle.
3. Uruchom aplikację na emulatorze lub telefonie.

## Uwagi
Wyszukiwanie realizowane jest po parametrze `name`, z krótkim debounce 350 ms.
