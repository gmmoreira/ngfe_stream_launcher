import com.gmmoreira.PosixFileReader
import com.gmmoreira.PosixLogger
import com.gmmoreira.WindowsProcessManager

fun main(args: Array<String>) {
    val logger = PosixLogger("launcher_log.txt")
    LauncherCommand(logger, PosixFileReader(), WindowsProcessManager(logger)).main(args)
}
