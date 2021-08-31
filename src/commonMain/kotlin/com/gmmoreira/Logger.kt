package com.gmmoreira

expect class Logger(filename: String = "log.txt") {
    fun info(message: String)
    fun error(message: String)
}