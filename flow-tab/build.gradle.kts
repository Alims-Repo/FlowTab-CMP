import com.vanniktech.maven.publish.SonatypeHost
import org.gradle.kotlin.dsl.support.kotlinCompilerOptions

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
//    alias(libs.plugins.androidLint)

    id("com.vanniktech.maven.publish") version "0.28.0"
}

android {
    namespace = "io.github.alimsrepo.flowtab"
    compileSdk = 37

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        compose = true
    }
}

kotlin {
//    androidLibrary {
//        namespace = "io.github.alimsrepo.flowtab"
//        compileSdk = 36
//        minSdk = 24
//
//        withHostTestBuilder {
//        }
//
//        withDeviceTestBuilder {
//            sourceSetTreeName = "test"
//        }.configure {
//            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        }
//    }

    androidTarget {
        publishLibraryVariants("release")
    }

    iosArm64 { binaries.framework { baseName = "FlowTabKit" } }
    iosSimulatorArm64 { binaries.framework { baseName = "FlowTabKit" } }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)

                implementation(libs.runtime)
                implementation(libs.foundation)
                implementation(libs.material3)
                implementation(libs.ui)

                implementation(libs.haze)
            }
        }

//        getByName("androidDeviceTest") {
//            dependencies {
//                implementation(libs.androidx.runner)
//                implementation(libs.androidx.core)
//                implementation(libs.androidx.testExt.junit)
//            }
//        }

        iosMain {
            dependencies {

            }
        }
    }
}

mavenPublishing {
    coordinates(
        groupId = "io.github.alims-repo",
        artifactId = "flowtab-cmp",
        version = "0.5.6-beta"
    )

    pom {
        name.set("Flow-Tab-CMP")
        description.set("A beautiful, animated, and completely framework-agnostic bottom navigation bar for Jetpack Compose and Compose Multiplatform")
        inceptionYear.set("2025")
        url.set("https://github.com/Alims-Repo/flowtab-cmp")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("alim")
                name.set("Alim Sourav")
                email.set("sourav.0.alim@gmail.com")
            }
        }

        scm {
            url.set("https://github.com/Alims-Repo/flowtab-cmp")
            connection.set("scm:git:git://github.com/Alims-Repo/flowtab-cmp.git")
            developerConnection.set("scm:git:ssh://github.com/Alims-Repo/flowtab-cmp.git")
        }
    }

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()
}