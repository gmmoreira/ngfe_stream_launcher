package com.gmmoreira

import kotlinx.cinterop.*
import platform.windows.*

class WindowsProcessManager(private val logger: Logger): ProcessManager {
    override fun createProcess(config: Configuration): Process {
        val application = config.application
        val joinedArgs = config.args.joinToString(" ")
        val cwd = config.cwd

        logger.info("Launching $application")
        logger.info("Arguments: \"$joinedArgs\"")
        cwd?.let { logger.info("Current Working Directory: $it")}

        memScoped {
            val flags = (NORMAL_PRIORITY_CLASS or CREATE_NEW_CONSOLE or CREATE_NEW_PROCESS_GROUP).toUInt()
            val startupInfo = alloc<STARTUPINFO>()
            val processInformation = alloc<PROCESS_INFORMATION>()
            val cmdLine = "\"$application\" $joinedArgs"
            val result = CreateProcess?.let {
                it(null, cmdLine.wcstr.ptr, null, null, 0, flags,
                    null, cwd?.wcstr?.ptr, startupInfo.ptr, processInformation.ptr) != 0
            } ?: false

            if (result) {
                processInformation.let {
                    logger.run { info("Process ${it.dwProcessId} for $application") }
                    CloseHandle(it.hProcess)
                    CloseHandle(it.hThread)
                }
            }
        }
    }

    override fun waitProcessExit(process: Process) {
        memScoped {
            val hProcess = OpenProcess(PROCESS_QUERY_INFORMATION, false, process.pid)
            WaitForSingleObject(hProcess, INFINITE)
            CloseHandle(hProcess)
        }
    }
}