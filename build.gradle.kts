// import 문이 있던 자리
import org.gradle.api.tasks.Delete

plugins {
    id("com.android.application") version "8.9.2" apply false
    id("com.android.library")     version "8.9.2" apply false
    kotlin("android")             version "2.1.0" apply false
    kotlin("kapt")                version "2.1.0" apply false
}

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
}
