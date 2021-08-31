plugins {
    kotlin("multiplatform") version "1.5.30"
}

group = "me.guilh"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "main"
                if (isMingwX64) {
                    //linkerOpts("-Wl,--subsystem,windows")
                    //runTask?.args("-h")
                    runTask?.args("--cwd", "E:\\Guilherme", "C:\\Windows\\System32\\notepad.exe", "a.txt", "b.txt")
                }
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.github.ajalt.clikt:clikt:3.2.0")
            }
        }
        val nativeMain by getting
        val nativeTest by getting
    }
}
