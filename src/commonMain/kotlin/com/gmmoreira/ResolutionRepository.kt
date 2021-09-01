package com.gmmoreira

expect class ResolutionRepository(logger: Logger? = null) {
    // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-enumdisplaysettingsw
    fun getResolutions(device: DisplayDevice): List<Resolution>

    // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-enumdisplaysettingsw
    fun getCurrentResolution(device: DisplayDevice): Resolution?

    // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-changedisplaysettingsexw
    fun applyResolution(device: DisplayDevice, resolution: Resolution)
}
