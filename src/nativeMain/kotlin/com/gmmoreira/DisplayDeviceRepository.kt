package com.gmmoreira

import kotlinx.cinterop.*
import platform.windows.*

actual class DisplayDeviceRepository actual constructor(private val logger: Logger?) {
    // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-enumdisplaydevicesw
    actual fun attachedDevices(): List<DisplayDevice> {
        val list = mutableListOf<DisplayDevice>()
        var deviceIndex = 0u
        var result = 1
        while (result == 1) {
            memScoped {
                val device = alloc<DISPLAY_DEVICE>()
                val pDevice = device.ptr
                device.cb = sizeOf<DISPLAY_DEVICE>().toUInt()
                result =
                    EnumDisplayDevices?.let { it(null, deviceIndex, pDevice, EDD_GET_DEVICE_INTERFACE_NAME.toUInt()) }
                        ?: 0
                if (result == 1) {
                    val attachedToDesktop = device.StateFlags and DISPLAY_DEVICE_ATTACHED_TO_DESKTOP.toUInt()
                    if (attachedToDesktop != 0u) {
                        logger?.run { info("Attached: ${device.DeviceName.toKString()}") }
                        logger?.run { info(device.DeviceString.toKString()) }
                        list.add(DisplayDevice(device.DeviceName.toKString(), device.DeviceString.toKString()))
                    }
                }
            }

            if (result == 0) {
                break
            }
            deviceIndex += 1u
        }

        return list

    }

    // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-enumdisplaydevicesw
    actual fun primaryDevice(): DisplayDevice? {
        memScoped {
            val device = alloc<DISPLAY_DEVICE>()
            val pDevice = device.ptr
            device.cb = sizeOf<DISPLAY_DEVICE>().toUInt()
            var deviceIndex = 0u
            while(true) {
                val result =
                    EnumDisplayDevices?.let { it(null, deviceIndex, pDevice, EDD_GET_DEVICE_INTERFACE_NAME.toUInt()) }
                        ?: 0
                if (result == 1) {
                    val isPrimaryDevice = device.StateFlags and DISPLAY_DEVICE_PRIMARY_DEVICE.toUInt() != 0u
                    if (isPrimaryDevice) {
                        logger?.run { info("Primary: ${device.DeviceName.toKString()}") }
                        logger?.run { info(device.DeviceString.toKString()) }

                        return DisplayDevice(device.DeviceName.toKString(), device.DeviceString.toKString())
                    }
                } else
                    break
            }
        }
        return null
    }
}