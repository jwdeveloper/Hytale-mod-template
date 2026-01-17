# Hytale Mod Template

This is a template project for creating mods for Hytale using Java. It includes a basic project structure, a manifest file, and a pre-configured IntelliJ IDEA run configuration to help you get started quickly.

## Prerequisites

Before you begin, ensure you have the following installed:
- **Hytale**: Installed via the official launcher. The build script looks for Hytale in its default installation path (`%AppData%\Roaming\Hytale`).
- **Java Development Kit (JDK)**: Version 25 or newer is recommended (matching the `java_version` in `gradle.properties`).
- **IntelliJ IDEA**: Recommended for the best development experience, as this template includes specific IDEA configurations.

## Getting Started

To set up your mod using this template, follow these steps:

### 1. Fork this Repository

Start by forking this repository to your own GitHub account. This allows you to have your own copy of the template to work on and save your changes.

### 2. Project Configuration

Update the project identity in the following files:

- **`settings.gradle`**: Change `rootProject.name` to your desired project name.
  ```gradle
  rootProject.name = 'YourModName'
  ```
- **`gradle.properties`**: Update the version and other properties as needed.
  ```properties
  version=1.0.0
  java_version=25
  ```

### 3. Mod Manifest

The `src/main/resources/manifest.json` file contains metadata about your mod. Update the following fields:
- `Group`: Your organization or personal group name (e.g., `com.example`).
- `Name`: The display name of your mod.
- `Description`: A brief description of what your mod does.
- `Authors`: Your name or handle.
- `Main`: The fully qualified name of your main class (e.g., `com.example.yourmod.Main`).

### 4. Refactoring Code

By default, the template uses the package `com.yourgroup.yourmod`. You should rename this to match your `Group` and `Name`:

1.  In IntelliJ IDEA, right-click on the `com.yourgroup.yourmod` package in `src/main/java`.
2.  Select **Refactor** > **Rename...**.
3.  Rename the package parts to match your desired structure.
4.  Ensure that the `Main` class package declaration and the `Main` field in `manifest.json` are updated accordingly.

### 5. Running Your Mod

This template comes with a pre-configured Run Configuration for IntelliJ IDEA called **"ModHytaleServer"**.

1.  Open the project in IntelliJ IDEA.
2.  Wait for Gradle to sync.
3.  Select **ModHytaleServer** from the run configurations dropdown at the top right.
4.  Click the **Run** or **Debug** icon.

The build script will automatically:
- Create a `run` directory in your project folder.
- Update the `manifest.json` with the latest version from `gradle.properties`.
- Launch the Hytale server with your mod loaded.

## Project Structure

- `src/main/java`: Your Java source code.
- `src/main/resources/manifest.json`: The mod's metadata file.
- `build.gradle`: Gradle build script containing logic for dependency management and IDEA run configurations.
- `gradle.properties`: Project-wide properties.
- `run/`: (Generated) The working directory for the Hytale server.

## Troubleshooting

- **Hytale Not Found**: If the build fails because it cannot find `HytaleServer.jar`, ensure Hytale is installed in the default directory (`%AppData%\Roaming\Hytale`). You can adjust the `hytaleHome` variable in `build.gradle` if you have it installed elsewhere.
- **Java Version**: Ensure your IDE is configured to use the JDK version specified in `gradle.properties`.
