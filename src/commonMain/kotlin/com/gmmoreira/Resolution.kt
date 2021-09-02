package com.gmmoreira

data class Resolution(val width: UInt, val height: UInt, val refreshRate: UInt) {
    override fun toString(): String = "${width}x${height}@${refreshRate}"
}