package com.gmmoreira

import kotlinx.cinterop.*
import platform.windows.*

actual class ResolutionRepository actual constructor(private val readOnly: Boolean, private val logger: Logger?) {
    // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-enumdisplaysettingsw
    actual fun getResolutions(device: DisplayDevice): List<Resolution> {
        val resolutions = mutableListOf<Resolution>()

        memScoped {
            val devMode = alloc<DEVMODE>()
            devMode.dmSize = sizeOf<DEVMODE>().toUShort()

            var index = 0u
            while(true) {
                val result = EnumDisplaySettings?.let { it(device.name.wcstr.ptr, index, devMode.ptr) } ?: 0

                if (result != 0) {
                    resolutions.add(Resolution(devMode.dmPelsWidth, devMode.dmPelsHeight, devMode.dmDisplayFrequency))
                } else
                    break

                index += 1u
            }
        }

        return resolutions
    }

    // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-enumdisplaysettingsw
    actual fun getCurrentResolution(device: DisplayDevice): Resolution? {
        memScoped {
            val devmode = alloc<DEVMODE>()
            devmode.dmSize = sizeOf<DEVMODE>().toUShort()

            val result = EnumDisplaySettings?.let { it(device.name.wcstr.ptr, ENUM_CURRENT_SETTINGS, devmode.ptr) } ?: 0

            if (result != 0) {
                return Resolution(devmode.dmPelsWidth, devmode.dmPelsHeight, devmode.dmDisplayFrequency)
            }
        }

        return null
    }

    // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-changedisplaysettingsexw
    actual fun applyResolution(device: DisplayDevice, resolution: Resolution) {
        memScoped {
            val devmode = alloc<DEVMODE>()
            devmode.dmSize = sizeOf<DEVMODE>().toUShort()

            val result = EnumDisplaySettings?.let { it(device.name.wcstr.ptr, ENUM_CURRENT_SETTINGS, devmode.ptr) } ?: 0

            if (result != 0) {
                devmode.apply {
                    dmPelsWidth = resolution.width
                    dmPelsHeight = resolution.height
                    dmDisplayFrequency = resolution.refreshRate
                }

                val dynamic = 0u

                if(!readOnly) {
                    val changed = ChangeDisplaySettingsEx?.let {
                        it(device.name.wcstr.ptr, devmode.ptr, null, dynamic, null) == DISP_CHANGE_SUCCESSFUL
                    } ?: false

                    if (changed) {
                        val newResolution = getCurrentResolution(device)
                        logger?.run { info("New resolution is: $newResolution") }
                    }
                } else
                    logger?.run { info("Repository is read-only") }
            }
        }
    }
}
