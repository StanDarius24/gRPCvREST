package com.stannis.protobufbench.mapper


interface MemoryTest {
    fun bytesToMegabytes(bytes: Long): Long {
        return bytes
        //        return bytes / MEGABYTE;
    }

    companion object {
        const val MEGABYTE = 1024L * 1024L
    }
}

