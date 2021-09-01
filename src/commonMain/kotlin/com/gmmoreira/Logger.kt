package com.gmmoreira

interface Logger {
    fun info(message: String) = write(formatMessage(message, "INFO"))
    fun error(message: String) = write(formatMessage(message, "ERROR"))
    fun formatMessage(message: String, level: String): String = "[${currentTime()}] $level --: $message\n"
    fun currentTime(): String
    fun write(message: String)
    fun close()
}