buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

rootProject.name = "DogFoodScanner"
include(":app")

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
