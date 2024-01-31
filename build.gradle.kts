// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra.apply {
        set("kotlin_version", "1.8.0")
        set("nav_version", "2.5.3")
        set("room_version", "2.4.2")
        set("lifecycle_version", "2.5.1")
    }
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        val navVersion = rootProject.extra.get("nav_version") as String
        val kotlinVersion = rootProject.extra.get("kotlin_version") as String

        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
        classpath("com.android.tools.build:gradle:7.4.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}