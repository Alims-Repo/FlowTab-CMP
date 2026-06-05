import com.vanniktech.maven.publish.SonatypeHost
import com.vanniktech.maven.publish.KotlinMultiplatform

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)

    id("com.vanniktech.maven.publish") version "0.30.0"
    id("signing")
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
                implementation(libs.haze.blur)
            }
        }

        iosMain {
            dependencies {

            }
        }
    }
}

mavenPublishing {
    configure(KotlinMultiplatform(sourcesJar = true))

    coordinates(
        groupId = "io.github.alims-repo",
        artifactId = "flowtab-cmp",
        version = "0.5.9-beta"
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
}

// Read signing credentials from ~/.gradle/gradle.properties
// Keys: signingInMemoryKeyId, signingInMemoryKey, signingInMemoryKeyPassword
val signingKeyId = findProperty("signingInMemoryKeyId") as String?
val signingKey = findProperty("signingInMemoryKey") as String?
val signingPassword = findProperty("signingInMemoryKeyPassword") as String? ?: ""

signing {
    if (signingKey != null) {
        useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
        sign(publishing.publications)
    } else {
        logger.warn("⚠️  signingInMemoryKey not set — publications will NOT be signed.")
    }
}