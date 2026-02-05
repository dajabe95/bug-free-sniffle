pluginManagement {
    repositories {
        gradlePluginPortal()
        maven { url = uri("https://maven.google.com") }
        mavenCentral()
    }
}

rootProject.name = "DogFoodScanner"
include(":app")

buildscript {
    repositories {
        maven { url = uri("https://maven.google.com") }
        mavenCentral()
    }
}

allprojects {
    repositories {
        maven { url = uri("https://maven.google.com") }
        mavenCentral()
    }
}
