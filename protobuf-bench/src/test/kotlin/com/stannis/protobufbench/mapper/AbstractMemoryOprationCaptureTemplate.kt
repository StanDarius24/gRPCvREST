package com.stannis.protobufbench.mapper

import org.slf4j.LoggerFactory

/**
 * Attempt to determine how much memory is used during a test
 */
class AbstractMemoryOprationCaptureTemplate internal constructor() : MemoryTest {
    private val runtime: Runtime

    init {
        runtime = Runtime.getRuntime()
    }

    protected fun logMemoryUsage(memory: Long, after: Long, Message: String) {
        LOG.info("$Message = {} bytes", bytesToMegabytes(after - memory))
    }

    protected val memoryAfterOperation: Long
        protected get() = runtime.totalMemory() - runtime.freeMemory()
    protected val initialMemory: Long
        protected get() {
            runtime.gc()
            return runtime.totalMemory() - runtime.freeMemory()
        }

    companion object {
        private val LOG = LoggerFactory.getLogger(AbstractMemoryOprationCaptureTemplate::class.java)
    }
}

