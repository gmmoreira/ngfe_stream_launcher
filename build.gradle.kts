plugins {
    kotlin("multiplatform") version "1.5.30"
    kotlin("plugin.serialization") version "1.5.30"
}

group = "me.guilh"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
/*
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
                    runTask?.args("--help")
                    //runTask?.args("--cwd", "E:\\Guilherme", "C:\\Windows\\System32\\notepad.exe", "a.txt", "b.txt")
                }
            }
        }
    }
*/

    val windowsTarget = mingwX64("windows")
    val linuxTarget = linuxX64("linux")
    val macTarget = macosX64("macos")

    val nativeTarget = when {
        hostOs == "Mac OS X" -> macTarget
        hostOs == "Linux" -> linuxTarget
        isMingwX64 -> windowsTarget
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "main"
                runTask?.args("--help")
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.github.ajalt.clikt:clikt:3.2.0")
                implementation("net.mamoe.yamlkt:yamlkt:0.10.2")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val windowsMain by getting
        val windowsTest by getting
        val linuxMain by getting
        val linuxTest by getting
        val macosMain by getting
        val macosTest by getting
    }
}
