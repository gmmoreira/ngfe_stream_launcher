import com.gmmoreira.PosixFileReader
import com.gmmoreira.PosixLogger

fun main(args: Array<String>) = LauncherCommand(PosixLogger("launcher_log.txt"), PosixFileReader()).main(args)
