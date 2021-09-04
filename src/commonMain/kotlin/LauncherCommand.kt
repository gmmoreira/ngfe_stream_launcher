import com.gmmoreira.*

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.UsageError
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option

class LauncherCommand(val logger: Logger, val fileReader: FileReader, val processManager: ProcessManager) : CliktCommand() {
    val application by argument().default("")
    val args by argument().multiple()
    val cwd: String? by option(help = "set current working directory")
    val configFile: String? by option("-c", "--config", help = "load configuration file")
    val width: UInt? by option("-w", "--width", help = "display width").convert { it.toUInt() }
    val height: UInt? by option("-h", "--height", help = "display height").convert { it.toUInt() }
    val refreshRate: UInt? by option("-r", "--refresh-rate", help = "display refresh rate").convert { it.toUInt() }
    val dryRun: Boolean? by option("-n", "--dry-run", help = "log what would be done").flag()

    override fun run() {
        val displayDeviceRepository = DisplayDeviceRepository(logger)
        val resolutionRepository = ResolutionRepository(logger)
        val configuration =  Configuration()
            .loadConfigFile(configFile, fileReader)
            .loadCommandLine(application, args, width, height, refreshRate, cwd, dryRun)

        if (configuration.application.isEmpty()) {
            throw UsageError("APPLICATION argument cannot be empty")
        }

        val primaryDevice = displayDeviceRepository.primaryDevice()
        val currentResolution = primaryDevice?.let { resolutionRepository.getCurrentResolution(it) }
        val targetResolution = configuration.resolution()
        currentResolution?.let { logger.run { info("Current resolution: $it") } }
        targetResolution?.let { logger.run { info("Target resolution: $it") } }

        if (primaryDevice != null && targetResolution != null) {
            logger.run { info("Changing resolution to $targetResolution") }

            if (!configuration.dryRun)
                resolutionRepository.applyResolution(primaryDevice, targetResolution)
        }

        if (!configuration.dryRun) {
            val process: Process = processManager.createProcess(configuration)
            processManager.waitProcessExit(process)
        }

        if (primaryDevice != null && currentResolution != null) {
            logger.run { info("Restoring original resolution $currentResolution") }

            if (!configuration.dryRun)
                resolutionRepository.applyResolution(primaryDevice, currentResolution)
        }
    }
}