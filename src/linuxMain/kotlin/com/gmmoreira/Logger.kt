package com.gmmoreira

import kotlinx.cinterop.*
import platform.posix.*

actual class Logger actual constructor(private val filename: String) {
    private var fileHandle: Int? = null

    init {
        val mode = S_IRUSR or S_IWUSR or S_IRGRP or S_IROTH
        val file = open(filename, O_WRONLY or O_CREAT or O_APPEND, mode)
        if (file != -1)
            fileHandle = file
    }

    actual fun info(message: String) {
        writeToFile(message, "INFO")
    }

    actual fun error(message: String) {
        writeToFile(message, "ERROR")
    }

    private fun writeToFile(message: String, level: String) {
        fileHandle?.let {
            memScoped {
                val formattedMessage = formatMessage(message, "INFO")
                println(formattedMessage)
                val buffer = formattedMessage.cstr
                // remove 1 from buffer size to not write NULL character at the end of string
                val count = if (buffer.size > 0) buffer.size.toULong() - 1u else 0u
                write(it, buffer, count)
            }
        }
    }

    private fun currentTime(): String {
        memScoped {
            val t = alloc<LongVar>()
            time(t.ptr)
            return ctime(t.ptr)?.toKString()?.trim() ?: ""
        }
    }

    private fun isoTime(): String {
        memScoped {
            val t = alloc<LongVar>()
            time(t.ptr)
            val tm = localtime(t.ptr)
            val buffer = allocArray<ByteVar>(256)
            val format = "%Y-%m-%dT%H:%M:%S%z"
            strftime(buffer, 256, format, tm)
            return buffer.toKString().trim()
        }
    }

    private fun formatMessage(message: String, level: String): String {
        return "[${currentTime()}] $level --: $message\n"
    }

    fun close() {
        fileHandle?.let(::close)
    }
}