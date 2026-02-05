buildscript {
    repositories {
        maven { url = uri("https://maven.google.com") }
        mavenCentral()
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven { url = uri("https://maven.google.com") }
        mavenCentral()
    }
}

rootProject.name = "DogFoodScanner"
include(":app")

allprojects {
    repositories {
        maven { url = uri("https://maven.google.com") }
        mavenCentral()
    }
}
