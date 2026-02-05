plugins {
    id("com.android.application") version "4.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.5.31" apply false
    id("org.jetbrains.kotlin.kapt") version "1.5.31" apply false
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
