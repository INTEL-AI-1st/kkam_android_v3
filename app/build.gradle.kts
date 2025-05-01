import java.util.Properties
import org.gradle.api.tasks.Delete

// local.properties로부터 Supabase URL/Key 로드
val localProps = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}
val supabaseUrl: String = localProps.getProperty("supabase.url")
val supabaseKey: String = localProps.getProperty("supabase.key")

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    // Kotlin 2.0+ Compose Compiler 플러그인 추가
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0"
}

configurations.all {
    // Kotlin 표준 라이브러리 강제 버전 맞춤
    resolutionStrategy.force("org.jetbrains.kotlin:kotlin-stdlib:2.1.0")
    resolutionStrategy.force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.1.0")
    resolutionStrategy.force("org.jetbrains.kotlin:kotlin-stdlib-jdk7:2.1.0")
    resolutionStrategy.force("org.jetbrains.kotlin:kotlin-reflect:2.1.0")
}

android {
    namespace = "com.example.kkam_backup"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.kkam_backup"
        minSdk = 34
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Supabase 정보 BuildConfig에 주입
        buildConfigField("String", "SUPABASE_URL", "\"$supabaseUrl\"")
        buildConfigField("String", "SUPABASE_KEY", "\"$supabaseKey\"")
    }

    buildFeatures {
        compose     = true
        buildConfig = true
    }

    composeOptions {
        // Compose 컴파일러 확장 버전도 Kotlin 플러그인과 동일하게 설정
        kotlinCompilerExtensionVersion = "2.1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
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
}

dependencies {
    // Kotlin 표준 라이브러리
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.1.0")

    // AndroidX
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.activity:activity-ktx:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Compose 라이브러리
    val composeVersion = "1.4.0"
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.activity:activity-compose:1.7.0")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")

    // Supabase & Ktor Client
    implementation(platform("io.github.jan-tennert.supabase:bom:3.1.4"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.ktor:ktor-client-android:2.3.4")

    // TensorFlow Lite
    implementation("org.tensorflow:tensorflow-lite:2.11.0")
    implementation("org.tensorflow:tensorflow-lite-support:0.4.3")

    // CameraX
    implementation("androidx.camera:camera-core:1.2.2")
    implementation("androidx.camera:camera-camera2:1.2.2")
    implementation("androidx.camera:camera-lifecycle:1.2.2")
    implementation("androidx.camera:camera-view:1.2.2")

    // WebView & Preference
    implementation("androidx.webkit:webkit:1.6.1")
    implementation("androidx.preference:preference-ktx:1.2.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
