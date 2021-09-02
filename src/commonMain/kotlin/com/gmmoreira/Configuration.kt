package com.gmmoreira

import kotlinx.serialization.Serializable
import net.mamoe.yamlkt.Yaml

@Serializable
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

    fun loadConfigFile(configFile: String?, fileReader: FileReader): Configuration {
        return configFile?.let {
            val loadedConfig = Yaml.decodeFromString(Configuration.serializer(),
                fileReader.readText(configFile))
            copy().let {
                if (loadedConfig.application.isNotEmpty()) it.copy(application = loadedConfig.application) else it
            }.let {
                if (loadedConfig.args.isNotEmpty()) it.copy(args = loadedConfig.args) else it
            }.let {
                if (loadedConfig.width != null) it.copy(width = loadedConfig.width) else it
            }.let {
                if (loadedConfig.height != null) it.copy(height = loadedConfig.height) else it
            }.let {
                if (loadedConfig.refreshRate != null) it.copy(refreshRate = loadedConfig.refreshRate) else it
            }.let {
                if (loadedConfig.cwd != null) it.copy(cwd = loadedConfig.cwd) else it
            }.let {
                if (loadedConfig.dryRun != dryRun) it.copy(dryRun = loadedConfig.dryRun) else it
            }
        } ?: copy()
    }

    fun loadCommandLine(application: String, args: List<String>, width: UInt?, height: UInt?,
                        refreshRate: UInt?, cwd: String?, dryRun: Boolean?): Configuration {
        return copy().let {
            if (application.isNotEmpty()) it.copy(application = application) else it
        }.let {
            if (args.isNotEmpty()) it.copy(args = args) else it
        }.let {
            if (width != null) it.copy(width = width) else it
        }.let {
            if (height != null) it.copy(height = height) else it
        }.let {
            if (refreshRate != null) it.copy(refreshRate = refreshRate) else it
        }.let {
            if (cwd != null) it.copy(cwd = cwd) else it
        }.let {
            if (dryRun != null) it.copy(dryRun = dryRun) else it
        }
    }
}