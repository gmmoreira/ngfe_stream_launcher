package com.gmmoreira

import kotlinx.cinterop.*
import platform.windows.*

actual class Launcher actual constructor(private val displayDeviceRepository: DisplayDeviceRepository, private val resolutionRepository: ResolutionRepository, private val logger: Logger?) {
    actual fun spawn(config: Configuration) {
        val primaryDevice = displayDeviceRepository.primaryDevice()
        val currentResolution = primaryDevice?.let { resolutionRepository.getCurrentResolution(it) }
        val targetResolution = config.resolution()
        currentResolution?.let { logger?.run { info("Current resolution: $it") } }
        targetResolution?.let { logger?.run { info("Target resolution: $it") } }

        if (primaryDevice != null && targetResolution != null) {
            logger?.run { info("Changing resolution to $targetResolution") }

            if (!config.dryRun)
                resolutionRepository.applyResolution(primaryDevice, targetResolution)
        }

        launch(config)

        if (primaryDevice != null && currentResolution != null) {
            logger?.run { info("Restoring original resolution $currentResolution") }

            if (!config.dryRun)
                resolutionRepository.applyResolution(primaryDevice, currentResolution)
        }
    }

    // https://docs.microsoft.com/en-us/windows/win32/api/processthreadsapi/nf-processthreadsapi-createprocessw
    // https://docs.microsoft.com/en-us/windows/win32/procthread/process-creation-flags
    // https://docs.microsoft.com/en-us/windows/win32/api/synchapi/nf-synchapi-waitforsingleobject
    actual fun launch(config: Configuration) {
        val application = config.application
        val joinedArgs = config.args.joinToString(" ")
        val cwd = config.cwd

        logger?.run {
            info("Launching $application")
            info("Arguments: \"$joinedArgs\"")
            cwd?.let { info("Current Working Directory: $it")}
        }

        if (config.dryRun) return

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
                processInformation.run {
                    logger?.run { info("Process $dwProcessId for $application") }
                    // println("Monitoring $dwProcessId for $timeout ms")

                    do {
                        val waitResult = WaitForSingleObject(hProcess, INFINITE)
                        // println("Result of wait $waitResult")

                        if (waitResult == WAIT_OBJECT_0) {
                            // println("Process $dwProcessId has terminated")
                        }
                    } while(waitResult == WAIT_TIMEOUT.toUInt())

                    CloseHandle(hProcess)
                    CloseHandle(hThread)
                }
            }
        }
    }
}