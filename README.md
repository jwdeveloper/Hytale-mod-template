# 🧱 Hytale Mod Template

A clean, batteries-included **Gradle + Kotlin DSL template** for building Hytale mods with a smooth local development
workflow.

This template is designed to remove friction:

- cross-platform (Windows / macOS / Linux)
- automatic asset syncing
- optional auto-deployment to the Hytale Mods folder
- sane defaults, explicit switches, no magic

---

## ✨ Features

- ✅ Kotlin DSL (`build.gradle.kts`)
- ✅ Automatic OS-aware Hytale path detection
- ✅ Uses locally installed **Hytale Assets.zip**
- ✅ Manifest templating via `gradle.properties`
- ✅ Optional auto-copy of built JAR to `Hytale/UserData/Mods`
- ✅ Optional asset sync back into `src/main/resources`
- ✅ Safe for CI (side-effects are opt-in)

---

## 🧠 Installation

## 1️⃣ Clone the Repository

### Option A — From IntelliJ (recommended)

1. Open **IntelliJ IDEA**
2. On the welcome screen, click **“Get from VCS”**
3. Paste the repository URL:

`https://github.com/jwdeveloper/Hytale-mod-template`

4. Choose a directory where the project should live
5. Click **Clone**

### Option B — From Terminal

If you prefer the terminal:

```bash
git clone https://github.com/jwdeveloper/Hytale-mod-template
cd Hytale-mod-template

