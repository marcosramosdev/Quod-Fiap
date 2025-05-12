plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "br.com.fiap.trabalhoquod"
    compileSdk = 35

    defaultConfig {
        applicationId = "br.com.fiap.trabalhoquod"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.play.services.mlkit.face.detection)
    implementation(libs.androidx.espresso.core)
    implementation(libs.play.services.mlkit.text.recognition.common)
    implementation(libs.play.services.mlkit.text.recognition)
    implementation(libs.androidx.activity.ktx)

    implementation(libs.okhttp)

    // Coil para carregamento de imagens
    implementation(libs.coil.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android.v139)
    implementation(libs.face.detection.v1615)
    implementation(libs.kotlinx.coroutines.play.services)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)



    implementation(libs.androidx.biometric)
    runtimeOnly(libs.face.detection)
    implementation(libs.androidx.camera.core)
    runtimeOnly(libs.androidx.camera.lifecycle)
    runtimeOnly(libs.androidx.camera.view)
    
    implementation (libs.coil.compose.v222)



    implementation(libs.androidx.camera.core.v150alpha03)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle.v150alpha03)
    implementation(libs.androidx.camera.video)
    implementation(libs.androidx.camera.view.v150alpha03)
    implementation(libs.androidx.camera.mlkit.vision)
    implementation(libs.androidx.camera.extensions)
    runtimeOnly(libs.androidx.biometric)

}