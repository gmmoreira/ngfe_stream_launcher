import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.option

class LauncherCommand: CliktCommand() {
    val executable by argument()
    val args by argument().multiple()
    val cwd: String? by option(help="set current working directory")

    override fun run() {
        val logger = Logger()
        val displayDeviceRepository = DisplayDeviceRepository(logger)
        val resolutionRepository = ResolutionRepository(true, logger)
        logger.info("Executable: $executable")
        logger.info("Args: $args")
        Launcher(displayDeviceRepository, resolutionRepository, logger).run {
            spawn(executable, args, cwd)
        }
    }
}