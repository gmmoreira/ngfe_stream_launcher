package com.gmmoreira

import kotlinx.cinterop.*
import platform.posix.*

class PosixLogger(filename: String): Logger {
    private val fileHandle by lazy {
        fopen(filename, "a+")
    }

    override fun write(message: String) {
        print(message)

        fileHandle?.let {
            fputs(message, fileHandle)
        }
    }

    override fun currentTime(): String {
        memScoped {
            val t = alloc<LongVar>()
            time(t.ptr)
            return ctime(t.ptr)?.toKString()?.trim() ?: ""
        }
    }

    override fun close() {
        fileHandle?.let(::fclose)
    }
}