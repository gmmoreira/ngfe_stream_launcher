package com.gmmoreira

interface ProcessManager {
    fun createProcess(config: Configuration): Process
    fun waitProcessExit(process: Process)
}