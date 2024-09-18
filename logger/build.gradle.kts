import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    id(libs.plugins.kotlin.android.library.get().pluginId)
    id(libs.plugins.maven.publish.get().pluginId)
}

android {
    namespace = "com.playground.logger"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

tasks.register<Jar>("androidSourcesJar") {
    archiveClassifier.set("sources")
    from(android.sourceSets)
    from(java.sourceSets)
}

publishing {

    repositories {
        maven {
            val keyPropertiesFile = rootProject.file("local.properties")
            val keyProperties = Properties()
            keyProperties.load(FileInputStream(keyPropertiesFile))

            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/nalivajr/modules-playground-logger")
            credentials {
                username = keyProperties.get("GIT_USER").toString()
                password = keyProperties.get("GIT_TOKEN").toString()
            }
        }
    }

    val aarGroupId = "com.example.playground"
    val aarVersionName = "1.0.1-SNAPSHOT"

    val arrArtifactId = "logger"

    publications {
        create<MavenPublication>("MavenPublication") {
            groupId = aarGroupId
            artifactId = arrArtifactId
            version = aarVersionName
            artifact("build/outputs/aar/$arrArtifactId-release.aar")
            pom {
                name = "playground-logger"
                description = "Playground Logger library"
            }
        }
    }
}