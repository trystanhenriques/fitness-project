# Fitness Tracker MVP (Data-Driven Coaching)

A modular Android fitness application that tracks workouts and provides context-aware coaching recommendations based on user feedback and progress history.

## Features

- **Workout Tracking**: Log exercises, weight, reps, and sets using a local SQLite database.
- **Form Check & Feedback**: An interactive "Form Check" system that asks about discomfort and analyzes free-text descriptions.
- **Dynamic Recommendation Engine**: Provides personalized coaching tips ("Next Steps") based on:
    - **Experience Tier**: Progresses from Beginner to Advanced based on total workout count.
    - **Specific Feedback**: Adjusts recommendations if the user reports issues with knees, lower back, shoulders, etc.
- **Weekly Plan Builder**: Generates 2-7 day training schedules based on high-level goals (Strength, Hypertrophy, Endurance).
- **History & Progress**: View past workouts and track progress for specific exercises.
- **Privacy & Safety**: Mandatory medical disclaimer and "Delete All History" functionality in Settings.

## Technical Details

- **Language**: Java
- **Database**: SQLite
- **Architecture**: Modular "Engine" pattern for logic (FormCheck, Recommendation).
- **Data Driven**: Coaching logic, questions, and plans are stored in JSON assets for easy updates without code changes.
- **Dependencies**: 
    - `com.google.android.material:material:1.11.0`
    - `androidx.appcompat:appcompat:1.6.1`

## How it Works

1. **Log a Workout**: Enter your lift details in the Tracker.
2. **Perform a Form Check**: Select an exercise and describe how it felt or select specific discomfort areas.
3. **Get Recommendations**: The engine combines your history (Experience Tier) with your current feedback to suggest corrective exercises or plan adjustments.
4. **Build a Plan**: Use the Plan Builder to generate a weekly schedule tailored to your goals.

## Project Structure

- `com.fitnessproject.core.engine`: Contains the core logic for recommendations and form evaluation.
- `com.fitnessproject.core.data`: Handles SQLite persistence and JSON data loading.
- `com.fitnessproject.ui`: Contains activity classes for the various app features (Tracker, History, Results, Settings).
- `assets/data`: JSON files containing the "intelligence" of the app (rules, questions, next steps).
