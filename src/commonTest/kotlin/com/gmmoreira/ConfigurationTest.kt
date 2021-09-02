package com.gmmoreira

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class ConfigurationTest {
    @Test
    fun itReturnsNullWhenWidthIsNull() {
        val res = Configuration(width = null, dryRun = false).resolution()
        assertNull(res)
    }

    @Test
    fun itReturnsNullWhenHeightIsNull() {
        val res = Configuration(height = null, dryRun = false).resolution()
        assertNull(res)
    }
    @Test
    fun itReturnsNullWhenRefreshRateIsNull() {
        val res = Configuration(refreshRate = null, dryRun = false).resolution()
        assertNull(res)
    }

    @Test
    fun itReturnsAnEqualCopyWhenPathIsNull() {
        val config = Configuration("cmd.exe", listOf("echo"), 1920u, 1080u, 60u, "C:\\", false)
        val newConfig = config.loadConfigFile(null)

        assertEquals(config, newConfig)
    }

    @Test
    fun itLoadContentsFromFile() {
    }
}