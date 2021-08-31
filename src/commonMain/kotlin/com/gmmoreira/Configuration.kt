package com.gmmoreira

data class Configuration(
    val application: String = "", val args: List<String> = emptyList(),
    val width: UInt? = null, val height: UInt? = null, val refreshRate: UInt? = null,
    val cwd: String? = null, val dryRun: Boolean = false
) {

    fun resolution(): Resolution? {
        if (width != null && height != null && refreshRate != null)
            return Resolution(width, height, refreshRate)
        return null
    }

    fun loadConfigFile(configFile: String?): Configuration {
        return copy()
    }
}