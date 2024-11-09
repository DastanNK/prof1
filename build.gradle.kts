plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false

    kotlin("plugin.serialization") version libs.versions.kotlin

    id("com.google.gms.google-services") version "4.4.2" apply false
}