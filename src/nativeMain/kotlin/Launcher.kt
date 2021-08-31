import kotlinx.cinterop.*
import platform.windows.*

class Launcher(val displayDeviceRepository: DisplayDeviceRepository, val resolutionRepository: ResolutionRepository, val logger: Logger? = null) {
    fun spawn(command: String, args: List<String>, cwd: String?) {
        val primaryDevice = displayDeviceRepository.primaryDevice()
        val resolution = Configuration().getOrDefault("")
        val currentResolution = primaryDevice?.let { resolutionRepository.getCurrentResolution(it) }
        currentResolution?.let { logger?.run { info("Current resolution: $it") } }
        primaryDevice?.let {
            logger?.run { info("Changing resolution to $resolution") }
            resolutionRepository.applyResolution(it, resolution)
        }
        launch(command, args, cwd)
        primaryDevice?.let { device ->
            currentResolution?.let { resolution ->
                logger?.run { info("Restoring original resolution $resolution") }
                resolutionRepository.applyResolution(device, resolution)
            }
        }
        logger?.let(Logger::close)
    }

    // https://docs.microsoft.com/en-us/windows/win32/api/processthreadsapi/nf-processthreadsapi-createprocessw
    // https://docs.microsoft.com/en-us/windows/win32/procthread/process-creation-flags
    // https://docs.microsoft.com/en-us/windows/win32/api/synchapi/nf-synchapi-waitforsingleobject
    fun launch(executable: String, args: List<String>, cwd: String?) {
        val joinedArgs = args.joinToString(" ")

        logger?.run {
            info("Launching $executable")
            info("Arguments: \"$joinedArgs\"")
            cwd?.let { info("Current Working Directory: $it")}
        }

        memScoped {
            val flags = (NORMAL_PRIORITY_CLASS or CREATE_NEW_CONSOLE or CREATE_NEW_PROCESS_GROUP).toUInt()
            val startupInfo = alloc<STARTUPINFO>()
            val processInformation = alloc<PROCESS_INFORMATION>()
            val cmdLine = "\"$executable\" $joinedArgs"
            val result = CreateProcess?.let {
                it(null, cmdLine.wcstr.ptr, null, null, 0, flags,
                    null, cwd?.wcstr?.ptr, startupInfo.ptr, processInformation.ptr) != 0
            } ?: false

            if (result) {
                processInformation.run {
                    logger?.run { info("Process $dwProcessId for $executable") }
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