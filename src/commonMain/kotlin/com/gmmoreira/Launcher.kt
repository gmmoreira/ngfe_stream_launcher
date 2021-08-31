package com.gmmoreira

expect class Launcher(displayDeviceRepository: DisplayDeviceRepository, resolutionRepository: ResolutionRepository, logger: Logger? = null) {
    fun spawn(config: Configuration)

    // https://docs.microsoft.com/en-us/windows/win32/api/processthreadsapi/nf-processthreadsapi-createprocessw
    // https://docs.microsoft.com/en-us/windows/win32/procthread/process-creation-flags
    // https://docs.microsoft.com/en-us/windows/win32/api/synchapi/nf-synchapi-waitforsingleobject
    fun launch(config: Configuration)
}