package com.gmmoreira

import kotlinx.cinterop.*

actual class Launcher actual constructor(private val displayDeviceRepository: DisplayDeviceRepository,
                                         private val resolutionRepository: ResolutionRepository,
                                         private val logger: Logger?) {
    actual fun spawn(config: Configuration) {
        val primaryDevice = displayDeviceRepository.primaryDevice()
        val currentResolution = primaryDevice?.let { resolutionRepository.getCurrentResolution(it) }
        val targetResolution = config.resolution()
        currentResolution?.let { logger?.run { info("Current resolution: $it") } }
        targetResolution?.let { logger?.run { info("Target resolution: $it") } }

        if (primaryDevice != null && targetResolution != null) {
            logger?.run { info("Changing resolution to $targetResolution") }

            if (!config.dryRun)
                resolutionRepository.applyResolution(primaryDevice, targetResolution)
        }

        launch(config)

        if (primaryDevice != null && currentResolution != null) {
            logger?.run { info("Restoring original resolution $currentResolution") }

            if (!config.dryRun)
                resolutionRepository.applyResolution(primaryDevice, currentResolution)
        }
    }

    // https://docs.microsoft.com/en-us/windows/win32/api/processthreadsapi/nf-processthreadsapi-createprocessw
    // https://docs.microsoft.com/en-us/windows/win32/procthread/process-creation-flags
    // https://docs.microsoft.com/en-us/windows/win32/api/synchapi/nf-synchapi-waitforsingleobject
    actual fun launch(config: Configuration) {
        logger?.run { info("launch() has no support") }
    }
}