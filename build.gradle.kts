plugins {
    id("com.android.application") version "8.3.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("org.jetbrains.kotlin.kapt") version "1.9.22" apply false
}

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
