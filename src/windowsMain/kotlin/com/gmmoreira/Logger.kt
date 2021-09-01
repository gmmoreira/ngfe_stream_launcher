package com.gmmoreira

import kotlinx.cinterop.*
import platform.posix.*

class WindowsLogger(private val filename: String) : Logger {
    private var fileHandle: Int? = null

    init {
        val mode = S_IRUSR or S_IWUSR or S_IRGRP or S_IROTH
        val file = open(filename, O_WRONLY or O_CREAT or O_APPEND, mode)
        if (file != -1)
            fileHandle = file
    }

    override fun write(message: String) {
        println(message)

        fileHandle?.let {
            memScoped {
                val buffer = message.cstr
                // remove 1 from buffer size to not write NULL character at the end of string
                val count = if (buffer.size > 0) buffer.size.toUInt() - 1u else 0u
                write(it, buffer, count)
            }
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
        fileHandle?.let(::close)
    }
}