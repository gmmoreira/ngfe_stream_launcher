package com.gmmoreira

import kotlinx.cinterop.*
import platform.posix.fclose
import platform.posix.feof
import platform.posix.fopen
import platform.posix.fread

class PosixFileReader: FileReader {
    override fun readText(path: String): String {
        memScoped {
            val filePointer = fopen(path, "r")
            val buffer = allocArray<ByteVar>(1024)
            val content = StringBuilder()
            do {
                val bytesRead = fread(buffer, 1, 4096, filePointer)
                content.append( buffer.readBytes(bytesRead.toInt()).toKString() )
            } while(feof(filePointer) == 0)
            fclose(filePointer)
            return content.toString()
        }
    }
}