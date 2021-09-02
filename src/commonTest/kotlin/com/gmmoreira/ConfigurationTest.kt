package com.gmmoreira

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class ConfigurationTest {
    val simpleConfig = Configuration("cmd.exe", listOf("echo"), 1920u, 1080u, 60u, "C:\\", false)

    @Test
    fun itReturnsNullWhenWidthIsNull() {
        val res = simpleConfig.copy(width = null).resolution()
        assertNull(res)
    }

    @Test
    fun itReturnsNullWhenHeightIsNull() {
        val res = simpleConfig.copy(height = null).resolution()
        assertNull(res)
    }
    @Test
    fun itReturnsNullWhenRefreshRateIsNull() {
        val res = simpleConfig.copy(refreshRate = null).resolution()
        assertNull(res)
    }

    @Test
    fun itReturnsResolutionWhenAllAttributesAreSet() {
        val res = simpleConfig.resolution()

        assertEquals(Resolution(1920u, 1080u, 60u), res)
    }

    @Test
    fun itReturnsAnEqualCopyWhenPathIsNull() {
        val config = simpleConfig
        val newConfig = config.loadConfigFile(null, PosixFileReader())

        assertEquals(config, newConfig)
    }

    @Test
    fun itLoadContentsFromFile() {
        val config = Configuration().loadConfigFile(ResourceLoader.fullPath("config.yml"), PosixFileReader())
        val expected = Configuration("C:\\Windows\\System32\\notepad.exe", listOf("a.txt", "b.txt"),
            1920u, 1080u, 60u, "C:\\Users")

        assertEquals(expected, config)
    }

    @Test
    fun itUpdatesAttributesFromCommandLine() {
        val config = Configuration().loadCommandLine("C:\\Windows\\System32\\notepad.exe", listOf("a.txt", "b.txt"),
            1920u, 1080u, 60u, "C:\\Users", true)
        val expected = Configuration("C:\\Windows\\System32\\notepad.exe", listOf("a.txt", "b.txt"),
            1920u, 1080u, 60u, "C:\\Users", true)

        assertEquals(expected, config)
    }
}