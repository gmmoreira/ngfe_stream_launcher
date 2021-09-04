package com.gmmoreira

import kotlinx.cinterop.*
import platform.linux.posix_spawn
import platform.posix.basename
import platform.posix.waitpid

class PosixProcessManager(val logger: Logger): ProcessManager {
    override fun createProcess(config: Configuration): Process {
        memScoped {
            val pid = alloc<IntVar>()
            val application = config.application
            val appBasename = basename?.let { it(application.cstr.ptr) }?.toKString()
            val argv = (listOf(appBasename) + config.args + listOf(null)).map { it?.cstr?.ptr }.toCValues()
            val env = listOf<String?>(null).map { it?.cstr?.ptr }.toCValues()

            val result = posix_spawn(pid.ptr, application, null, null, argv, env)
            logger.debug("posix_spawn return value: $result")
            logger.info("PID: ${pid.value}")
            return Process(pid.value.toULong())
        }
    }

    override fun waitProcessExit(process: Process) {
        logger.info("Waiting process ${process.pid}")
        memScoped {
            val status = alloc<IntVar>()
            val result = waitpid(process.pid.convert(), status.ptr, 0)
            logger.debug("waitpid return value: $result")
            logger.debug("waitpid status: ${status.value}")
        }
    }
}