plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.android")version "1.9.24"
}

android {
    namespace = "com.kisayama.findcolorcode"
    compileSdk = 34

    buildFeatures {
        viewBinding = true
        compose = true
    }

    defaultConfig {
        applicationId = "com.kisayama.findcolorcode"
        minSdk = 23
        targetSdk = 35
        versionCode = 2
        versionName = "1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {

    implementation(libs.androidx.runtime.saved.instance.state)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation("androidx.compose.ui:ui:1.6.8")
    implementation(libs.androidx.material3)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.runtime.livedata)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation("androidx.fragment:fragment-ktx:1.7.0")
    implementation(libs.androidx.constraintlayout)
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("com.squareup.moshi:moshi:1.15.1")
    testImplementation(libs.junit)

    //retrofit2
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    //coroutine
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")
    //ksp
    ksp("androidx.room:room-compiler:2.6.1")//KSPとRoom間の依存関係を追加(@でコードを自動生成)
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.1")//KSPとmoshi間の依存関係を追加

    implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
    implementation("androidx.cardview:cardview:1.0.0")
    val lifecycle_version = "2.8.4"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.compose.foundation:foundation-layout")


    //JetPackCompose
    //NavigationCompose
    implementation("androidx.navigation:navigation-compose:2.7.3")
    //Compose作成のためのmaterial3
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.material3:material3-window-size-class:1.2.1")
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.3.0-rc01")

    //テスト用の依存関係
    testImplementation("junit:junit:4.13.2")

    implementation("androidx.room:room-runtime:2.6.1") // Room
    ksp("androidx.room:room-compiler:2.6.1") // Room用のKSP
    implementation("com.squareup.moshi:moshi:1.12.0") // Moshi
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.12.0") // Moshi用のKSP

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
