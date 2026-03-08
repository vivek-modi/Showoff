# Recipe App

Welcome to the Recipe App! This is a modern Android project built to showcase how to fetch and display delicious recipes using the latest Android technologies.

## Architecture & Data Flow (UDF)
This project follows **Clean Architecture** principles and uses **Unidirectional Data Flow (UDF)**. 

**Why UDF?**
It keeps the UI predictable. Instead of data changing all over the place, it flows in one direction:
1.  **State**: The ViewModel holds the "Single Source of Truth" (the current state of the screen).
2.  **Display**: The UI (Compose) just looks at this state and shows it.
3.  **Events**: When you click something, it sends an "Event" to the ViewModel.
4.  **Update**: The ViewModel handles the logic, updates the state, and the UI automatically refreshes.

## Module Dependency Relation
The app is modularized to keep things tidy. Here is how they talk to each other:

```text
:app (The Glue)
 ├── :ui (Screens & ViewModels)
 │    └── :domain (Business Logic)
 ├── :data (Data Sources)
 │    ├── :domain
 │    └── :core:network (API Helpers)
 └── :core:coroutine (Threading Helpers)
```

*   **`:domain`** is the center of the universe—it depends on nothing.
*   **`:ui`** and **`:data`** both depend on **`:domain`**.
*   **`:app`** brings everyone together to make the app run.

## Project Structure & Where to Code
If you are wondering which module you need to touch:

*   **`:ui`**: Go here if you want to add a new screen, change a button, or update how data is shown.
*   **`:domain`**: Go here to define new data models or "Use Cases" (the rules of your app).
*   **`:data`**: Go here if you're adding a new API call or changing how we save data.
*   **`:core`**: Small utility modules for Networking and Coroutines that don't change often.
*   **`:app`**: Only touch this for app-wide settings like themes or dependency injection setup.

## Tech We Used
*   **Kotlin**: Our main programming language.
*   **Jetpack Compose**: For building the UI.
*   **Koin**: For Dependency Injection (connecting all these modules).
*   **Ktor**: For talking to the internet.
*   **Coil**: For loading images.
*   **Kotlin Coroutines**: For smooth background tasks.
*   **Navigation 3**: For moving between screens.

## How to Run the Project
To get this app running smoothly, follow these simple steps:

### 1. Requirements
*   **Android Studio**: Use the latest stable version (Ladybug or newer is recommended for the best experience).
*   **JDK**: Ensure you have JDK 17 or higher configured in your Android Studio settings.

### 2. Opening the Project
*   Clone this repository or download the source code.
*   In Android Studio, go to **File > Open** and select the root folder of this project.
*   Wait for the **Gradle Sync** to finish. You'll see an "Elephant" icon in the top right or a progress bar at the bottom.

### 3. Setting Up an Emulator
*   Open the **Device Manager** in Android Studio.
*   Click **Create Device**.
*   **Hardware**: Choose a modern device like **Pixel 8** or **Pixel 7**.
*   **System Image**: We recommend using **API Level 34 (UpsideDownCake)** or higher.
    *   *Note*: The app supports down to API Level 24, but higher is always better for performance.
*   Finish the setup and start the emulator.

### 4. Running the App
*   In the top toolbar, make sure the **`app`** module is selected in the run configurations dropdown.
*   Select your running emulator or a physical device connected via USB/WiFi.
*   Click the green **Run** button (or press `Shift + F10`).

That's it! Hope you enjoy exploring the code and finding some tasty recipes.
