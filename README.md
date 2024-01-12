# Kotlin Authentication with Google

This repository contains a sample Kotlin project demonstrating how to implement Google Sign-In in a Android application using Jetpack Compose.

## Overview

- **Technology Stack:** Kotlin, Android, Jetpack Compose
- **Authentication:** Firebase Authentication
- **Third-party Integration:** Google Sign-In

## Features

- One Tap sign-in with Google
- Firebase authentication with Google credentials
- Profile management with Firestore integration

## Getting Started

Follow these steps to set up and run the project locally:

1. Clone the repository:

    ```bash
    git clone https://github.com/fahad0samara/kotlin_auth_with_google.git
    ```

2. Open the project in Android Studio.

3. Set up Firebase:
    - Create a new Firebase project on the [Firebase Console](https://console.firebase.google.com/).
    - Add an Android app to your Firebase project and download the `google-services.json` file.
    - Place the `google-services.json` file in the `app` module of your Android project.

4. Configure Google Sign-In:
    - Obtain a web client ID from the [Google Cloud Console](https://console.cloud.google.com/).
    - Replace the `R.string.google_web_client_id` in the project with your obtained web client ID.

5. Build and run the application on an emulator or physical device.

## Project Structure

- `app` module: Main Android application module.
- `:app:src/main/java/com/example/kotlin_auth_with_google`: Contains the main source code.
- `:app:src/main/java/com/example/kotlin_auth_with_google/ui`: Contains Jetpack Compose UI components.
- `:app:src/main/java/com/example/kotlin_auth_with_google/di`: Contains Dagger Hilt modules.
- `:app:src/main/java/com/example/kotlin_auth_with_google/viewmodel`: Contains ViewModels.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

Feel free to explore, modify, and use the code in your projects!

