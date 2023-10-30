package com.stannis.protobufbench.benchmark

import org.openjdk.jmh.results.format.ResultFormatType
import org.openjdk.jmh.runner.Runner
import org.openjdk.jmh.runner.RunnerException
import org.openjdk.jmh.runner.options.OptionsBuilder
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.core.io.support.PropertiesLoaderUtils
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Base test harness for Benchmark tests.  This class contains the global configuration for all benchmark tests including
 * warmup iterations, operation iterations, forks, threads, output file and type.  Additionally, this
 */
@SpringBootApplication
class BenchmarkBase {

    companion object {

        @Throws(RunnerException::class, IOException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val properties = PropertiesLoaderUtils.loadAllProperties("benchmark.properties")
            val warmup = properties.getProperty("benchmark.warmup.iterations", "5").toInt()
            val iterations = properties.getProperty("benchmark.test.iterations", "5").toInt()
            val forks = properties.getProperty("benchmark.test.forks", "1").toInt()
            val threads = properties.getProperty("benchmark.test.threads", "1").toInt()
            val testClassRegExPattern =
                properties.getProperty("benchmark.global.testclassregexpattern", ".*Benchmark.*")
            val resultFilePrefix = properties.getProperty("benchmark.global.resultfileprefix", "jmh-")
            val resultsFileOutputType = ResultFormatType.JSON
            val opt = OptionsBuilder()
                .include(testClassRegExPattern)
                .warmupIterations(warmup)
                .measurementIterations(iterations)
                .forks(forks)
                .threads(threads)
                .shouldDoGC(true)
                .shouldFailOnError(true)
                .resultFormat(resultsFileOutputType)
                .result(buildResultsFileName(resultFilePrefix, resultsFileOutputType))
                .shouldFailOnError(true)
                .jvmArgs("-server")
                .build()
            Runner(opt).run()
        }

        private fun buildResultsFileName(resultFilePrefix: String, resultType: ResultFormatType): String {
            val date = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("mm-dd-yyyy-hh-mm-ss")
            val suffix: String = when (resultType) {
                ResultFormatType.CSV -> ".csv"
                ResultFormatType.SCSV ->                 // Semi-colon separated values
                    ".scsv"

                ResultFormatType.LATEX -> ".tex"
                ResultFormatType.JSON -> ".json"
                else -> ".json"
            }
            return String.format("target/%s%s%s", resultFilePrefix, date.format(formatter), suffix)
        }
    }
}
