// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.google.devtools.ksp")version "2.0.21-1.0.25" apply false
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
}
