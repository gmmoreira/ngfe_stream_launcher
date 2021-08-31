import kotlinx.cinterop.*
import platform.windows.*

class DisplayDevice(val name: String, val graphicsCard: String) {
    fun applyResolution(resolution: Resolution) {
        memScoped {
            val devMode = alloc<DEVMODE>()
            devMode.dmSize = sizeOf<DEVMODE>().toUShort()
            devMode.dmBitsPerPel = 32u
            devMode.dmPelsWidth = resolution.width
            devMode.dmPelsHeight = resolution.height
            devMode.dmDisplayFrequency = resolution.refreshRate
            devMode.dmFields = (DM_PELSWIDTH or DM_PELSHEIGHT or DM_DISPLAYFREQUENCY).toUInt()
            //ChangeDisplaySettingsEx(name.wcstr.ptr, devMode.ptr, null, )
        }
    }
}