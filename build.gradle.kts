import org.gradle.internal.os.OperatingSystem

plugins {
    `maven-publish`
    id("hytale-mod") version "0.+"
}

group = (findProperty("plugin_group") as String?) ?: "com.example"
version = (findProperty("plugin_version") as String?) ?: "0.1.0"

val uploadToModsAfterBuild: Boolean =
    (findProperty("upload_to_mods_after_build") as String?)
        ?.toBooleanStrictOrNull()
        ?: false

val javaVersion = 25

val os = OperatingSystem.current()
val appData = when {
    os.isWindows -> {
        System.getenv("APPDATA")
            ?: error("APPDATA is not set on Windows")
    }

    os.isMacOsX -> {
        "${System.getProperty("user.home")}/Library/Application Support"
    }

    else -> {
        System.getenv("XDG_DATA_HOME")
            ?: "${System.getProperty("user.home")}/.local/share"
    }
}
val hytaleAssets = file("$appData/Hytale/install/release/package/game/latest/Assets.zip")
val hytaleMods = file("$appData/Hytale/UserData/Mods")


repositories {
    mavenCentral()
    maven("https://maven.hytale-modding.info/releases") {
        name = "HytaleModdingReleases"
    }
}

dependencies {
    compileOnly(libs.jetbrains.annotations)
    compileOnly(libs.jspecify)

    if (hytaleAssets.exists()) {
        compileOnly(files(hytaleAssets))
    } else {
        // Optional: Print a warning so you know why it's missing
        logger.warn("Hytale Assets.zip not found at: ${hytaleAssets.absolutePath}")
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(javaVersion)
    }

    withSourcesJar()
}

tasks.named<ProcessResources>("processResources") {
    var replaceProperties = mapOf(
        "plugin_group" to findProperty("plugin_group"),
        "plugin_maven_group" to project.group,
        "plugin_name" to findProperty("plugin_name"),
        "plugin_version" to project.version,
        "server_version" to findProperty("server_version"),

        "plugin_description" to findProperty("plugin_description"),
        "plugin_website" to findProperty("plugin_website"),

        "plugin_main_entrypoint" to findProperty("plugin_main_entrypoint"),
        "plugin_author" to findProperty("plugin_author")
    )

    filesMatching("manifest.json") {
        expand(replaceProperties)
    }

    inputs.properties(replaceProperties)
}

hytale {

}

tasks.withType<Jar> {
    manifest {
        attributes["Specification-Title"] = rootProject.name
        attributes["Specification-Version"] = version
        attributes["Implementation-Title"] = project.name
        attributes["Implementation-Version"] =
            providers.environmentVariable("COMMIT_SHA_SHORT")
                .map { "${version}-${it}" }
                .getOrElse(version.toString())
    }
}

publishing {
    repositories {
        // This is where you put repositories that you want to publish to.
        // Do NOT put repositories for your dependencies here.
    }

    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

// IDEA no longer automatically downloads sources/javadoc jars for dependencies, so we need to explicitly enable the behavior.
idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

val syncAssets = tasks.register<Copy>("syncAssets") {
    group = "hytale"
    description = "Automatically syncs assets from Build back to Source after server stops."

    // Take from the temporary build folder (Where the game saved changes)
    from(layout.buildDirectory.dir("resources/main"))

    // Copy into your actual project source (Where your code lives)
    into("src/main/resources")

    // IMPORTANT: Protect the manifest template from being overwritten
    exclude("manifest.json")

    // If a file exists, overwrite it with the new version from the game
    duplicatesStrategy = DuplicatesStrategy.INCLUDE

    doLast {
        println("✅ Assets successfully synced from Game to Source Code!")
    }
}

val deployToHytaleMods = tasks.register<Copy>("deployToHytaleMods") {
    group = "hytale"
    description = "Builds and deploys the final plugin JAR into the Hytale Mods directory."

    // Ensure the jar is built first
    dependsOn(tasks.named("jar"))

    // Take the produced JAR
    from(tasks.named<Jar>("jar").map { it.archiveFile })

    // Target: Hytale Mods directory
    into(hytaleMods)

    doFirst {
        if (!hytaleMods.exists()) {
            hytaleMods.mkdirs()
            logger.lifecycle("📁 Created Hytale Mods directory: ${hytaleMods.absolutePath}")
        }
    }

    doLast {
        println("✅ Plugin JAR deployed to Hytale Mods:")
        println("   → ${hytaleMods.absolutePath}")
    }
}


if (uploadToModsAfterBuild) {
    tasks.named("build") {
        finalizedBy(deployToHytaleMods)
    }

    logger.lifecycle("✅ upload_to_mods_after_build=true → auto-deploy ENABLED")
} else {
    logger.lifecycle("ℹ️ upload_to_mods_after_build=false → auto-deploy DISABLED")
}

afterEvaluate {
    // Now Gradle will find it, because the plugin has finished working
    val targetTask = tasks.findByName("runServer") ?: tasks.findByName("server")

    if (targetTask != null) {
        targetTask.finalizedBy(syncAssets)
        logger.lifecycle("✅ specific task '${targetTask.name}' hooked for auto-sync.")
    } else {
        logger.warn("⚠️ Could not find 'runServer' or 'server' task to hook auto-sync into.")
    }
}
