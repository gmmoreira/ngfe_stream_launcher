package com.gmmoreira

actual class DisplayDeviceRepository actual constructor(private val logger: Logger?) {
    // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-enumdisplaydevicesw
    actual fun attachedDevices(): List<DisplayDevice> {
        logger?.run { info("attachedDevices() has no support") }

        return emptyList()
    }

    // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-enumdisplaydevicesw
    actual fun primaryDevice(): DisplayDevice? {
        logger?.run { info("primaryDevice() has no support") }

        return null
    }
}