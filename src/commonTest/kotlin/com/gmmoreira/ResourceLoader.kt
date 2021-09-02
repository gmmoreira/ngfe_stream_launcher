package com.gmmoreira

import kotlinx.cinterop.memScoped
import platform.posix.fopen

class ResourceLoader {
    companion object {
        fun fullPath() {

        }

        fun readFile(path: String) {
            memScoped {
                val filePointer = fopen(path, "r")
            }
        }
    }
}