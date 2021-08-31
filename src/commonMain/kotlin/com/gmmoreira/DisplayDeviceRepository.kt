package com.gmmoreira

expect class DisplayDeviceRepository(logger: Logger? = null) {
    // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-enumdisplaydevicesw
    fun attachedDevices(): List<DisplayDevice>

    // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-enumdisplaydevicesw
    fun primaryDevice(): DisplayDevice?
}