import com.gmmoreira.LinuxLogger
import com.gmmoreira.PosixFileReader

fun main(args: Array<String>) = LauncherCommand(LinuxLogger("launcher_log.txt"), PosixFileReader()).main(args)
