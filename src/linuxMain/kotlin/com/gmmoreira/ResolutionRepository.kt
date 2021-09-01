package com.gmmoreira

import kotlinx.cinterop.*

actual class ResolutionRepository actual constructor(private val logger: Logger?) {
    // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-enumdisplaysettingsw
    actual fun getResolutions(device: DisplayDevice): List<Resolution> {
        logger?.run { info("getResolutions() has no support") }

        return emptyList()
    }

    // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-enumdisplaysettingsw
    actual fun getCurrentResolution(device: DisplayDevice): Resolution? {
        logger?.run { info("getCurrentResolution() has no support") }

        return null
    }

    // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-changedisplaysettingsexw
    actual fun applyResolution(device: DisplayDevice, resolution: Resolution) {
        logger?.run { info("applyResolution() has no support") }
    }
}
