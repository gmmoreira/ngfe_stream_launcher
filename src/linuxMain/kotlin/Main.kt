import com.gmmoreira.PosixFileReader
import com.gmmoreira.PosixLogger
import com.gmmoreira.PosixProcessManager

fun main(args: Array<String>) {
    val logger = PosixLogger("launcher_log.txt")
    LauncherCommand(logger, PosixFileReader(), PosixProcessManager(logger)).main(args)
}
