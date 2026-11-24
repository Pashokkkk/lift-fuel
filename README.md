# Lift & Fuel

Android app that combines gym workout tracking and food logging for people new to strength training.
Users set a goal (lose weight, gain muscle or maintain), log meals and workouts, and get simple
recommendations that always explain *why* they are shown. All data is stored locally (Room), so the app works offline.

## Status

- ✅ **Data layer**: Room database (15 tables, 12 DAOs), type converters, calorie/macro targets (Mifflin-St Jeor)
- ✅ **Mock data**: 153 foods, 48 exercises, 7 workout templates; debug builds also get a demo user with 5 weeks of history
- ✅ **Unit tests**: 25 tests for seed data, converters and the calorie calculator
- ⏳ **UI**: screens with ViewModels (Today, Food, Workouts, Progress, Settings) — in progress
- ⏳ Barcode scanning (Open Food Facts), meal photo analysis (Gemini), reminders

## Tech stack

Kotlin · Jetpack Compose + Material 3 · MVVM (ViewModel + StateFlow) · Room 2.8 + KSP · min SDK 26

## Project structure

```
app/src/main/java/com/example/liftfuel/
├── data/local/     Room entities, DAOs, query models, database, converters
├── data/seed/      built-in catalog + demo history generator
├── domain/         business logic (NutritionCalculator, …)
└── ui/             Compose screens and ViewModels
```

## Run

1. Open the project in Android Studio and let Gradle sync.
2. Run the `app` configuration on an emulator or phone (Android 8.0+).
3. Unit tests: right-click `app/src/test/java` → **Run Tests**.

Debug builds start with the demo user "Alex"; uninstall the app to reset the data.
