import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        dependencies {
            classpath(Dependencies.androidGradlePlugin)
            classpath(Dependencies.Kotlin.gradlePlugin)
            classpath(Dependencies.Dagger.hiltPlugin)
            classpath(Dependencies.SqlDelight.sqlDelightPlugin)
        }
    }
}

plugins {
    id("com.github.ben-manes.versions") version "0.39.0"
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(url = "https://jitpack.io")
        maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
        if (Dependencies.Compose.version.endsWith("SNAPSHOT")) {
            maven(url = Dependencies.Compose.snapshotUrl)
        }
    }
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            useIR = true
            jvmTarget = Versions.java
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-Xopt-in=kotlin.RequiresOptIn",
                "-Xjvm-default=enable"
            )
        }
    }
}
