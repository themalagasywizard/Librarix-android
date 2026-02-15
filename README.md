# Librarix Android

**Librarix iOS to Android Conversion**

Android port of the Librarix iOS app, built with Jetpack Compose and Kotlin.

## Status

**Project initialized:** February 15, 2026  
**Progress:** Phase 1 (Project Setup) - In Progress

## Features

- 📚 Book library management
- 📖 Reading progress tracking
- 🔍 Discover new books via Open Library
- 📊 Reading statistics and goals
- 📝 Notes and annotations
- 🌙 Dark/Light theme support

## Architecture

- **Pattern:** MVVM + Clean Architecture
- **UI:** Jetpack Compose
- **Local Storage:** Room Database
- **Remote:** Supabase (PostgreSQL + Auth)
- **DI:** Hilt
- **Navigation:** Compose Navigation

## Getting Started

### Prerequisites

- Android Studio Hedgehog | 2023.1.1 or later
- JDK 17
- Gradle 8.5
- Kotlin 1.9.22

### Build

```bash
./gradlew assembleDebug
```

### Run Tests

```bash
./gradlew test
```

## Documentation

- [Conversion Plan](./ANDROID_CONVERSION_PLAN.md)
- [Checklist](./CHECKLIST.md)

## License

MIT