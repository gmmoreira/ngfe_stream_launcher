import kotlinx.cinterop.*
import platform.posix.*

class Logger(private val filename: String = "log.txt") {
    private var fileHandle: Int? = null

    init {
        val mode = S_IRUSR or S_IWUSR or S_IRGRP or S_IROTH
        val file = open(filename, O_WRONLY or O_CREAT or O_APPEND, mode)
        if (file != -1)
            fileHandle = file
    }

    fun info(message: String) {
        fileHandle?.let {
            memScoped {
                val formattedMessage = formatMessage(message, "INFO")
                println(formattedMessage)
                val buffer = formattedMessage.cstr
                val count = if (buffer.size > 0) buffer.size - 1 else 0
                write(it, buffer, count.toUInt())
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
            val bytesWritten = strftime(buffer, 256, format, tm)
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