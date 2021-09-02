import com.gmmoreira.PosixFileReader
import com.gmmoreira.WindowsLogger

fun main(args: Array<String>) = LauncherCommand(WindowsLogger("launcher_log.txt"), PosixFileReader()).main(args)
