package org.remdev.flog

import android.util.Log
import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy
import ch.qos.logback.core.util.FileSize
import ch.qos.logback.core.util.StatusPrinter
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.charset.Charset

class LogToFileTree private constructor(
    private var loggerName: String,
    private val logFileName: String,
    private val logsDir: String,
    private val fileSizeInBytes: Int,
    private val historyLength: Int,
    private val level: Level
) : Flog {
    lateinit var logger: Logger
    private fun configure() {
        val externalFilesDir =
            if (logsDir.endsWith(File.separator)) logsDir else logsDir + File.separator
        val logsDirectory = File(externalFilesDir)
        val fileOk = logsDirectory.exists() || logsDirectory.mkdirs()
        if (fileOk) {
            val logDirectory = logsDirectory.path
            configureLogger(logDirectory)
        } else {
            println(String.format("Could not log to file. Cannot access: %s", logsDirectory))
        }
    }

    private fun configureLogger(logDirectory: String) {
        // reset the default context (which may already have been initialized)
        // since we want to reconfigure it
        val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
        //loggerContext.reset()
        val rollingFileAppender = RollingFileAppender<ILoggingEvent>()
        rollingFileAppender.context = loggerContext
        //rollingFileAppender.isAppend = true
        rollingFileAppender.file = logDirectory + File.separator + logFileName + ".log"
        val rollingPolicy = SizeAndTimeBasedRollingPolicy<ILoggingEvent>()
        rollingPolicy.context = loggerContext
        rollingPolicy.setTotalSizeCap(FileSize((fileSizeInBytes * historyLength + 1).toLong()))
        rollingPolicy.setMaxFileSize(FileSize(fileSizeInBytes.toLong()))
        rollingPolicy.maxHistory = historyLength
        val fileNamePatternStr =
            logDirectory + File.separator + logFileName + ".%d{yyyy-MM-dd,UTC}.%i.zip"
        rollingPolicy.fileNamePattern = fileNamePatternStr
        rollingPolicy.setParent(rollingFileAppender) // parent and context required!
        rollingPolicy.isCleanHistoryOnStart = true

        rollingPolicy.start()
        val encoder = PatternLayoutEncoder()
        encoder.context = loggerContext
        encoder.charset = Charset.forName("UTF-8")
        encoder.pattern = "%date %level [%thread] %msg%n"
        encoder.start()
        rollingFileAppender.rollingPolicy = rollingPolicy
        rollingFileAppender.encoder = encoder
        rollingFileAppender.name = loggerName
        rollingFileAppender.start()

        // add the newly created appenders to the root logger;
        // qualify Logger to disambiguate from org.slf4j.Logger

        logger = LoggerFactory.getLogger(loggerName) as Logger
        logger.level = level
        logger.addAppender(rollingFileAppender)
        logger.isAdditive = false
        // print any status messages (warnings, etc) encountered in logback config
        StatusPrinter.print(loggerContext)
    }

    override fun log(priority: Int, tag: String?, message: String?, t: Throwable?) {
        val logMessage = String.format("%s: %s", tag, message)
        when (priority) {
            Log.DEBUG -> logger.debug(logMessage, t)
            Log.INFO -> logger.info(logMessage, t)
            Log.WARN -> logger.warn(logMessage, t)
            Log.ERROR -> logger.error(logMessage, t)
        }
    }

    class Builder {
        private var loggerName = "undefined"
        private var logFileName = "app-log.txt"
        private var logsDir = ""
        private var fileSizeKB = 0
        private var historyLength = 10
        private var level = Level.ALL

        fun logger(name: String): Builder {
            this.loggerName = name
            return this
        }

        fun logFileName(logFileName: String): Builder {
            this.logFileName = logFileName
            return this
        }

        fun logsDir(logsDir: String): Builder {
            this.logsDir = logsDir
            return this
        }

        fun fileSizeKB(fileSizeKB: Int): Builder {
            require(fileSizeKB > 0) { "File size should be greater than 0" }
            this.fileSizeKB = fileSizeKB * 1024
            return this
        }

        fun fileSizeMB(fileSizeMB: Int): Builder {
            require(fileSizeMB > 0) { "File size should be greater than 0" }
            fileSizeKB = fileSizeMB * 1024 * 1024
            return this
        }

        fun historyLength(historyLength: Int): Builder {
            this.historyLength = historyLength
            return this
        }

        fun level(level: Level): Builder {
            this.level = level
            return this
        }

        fun build(): LogToFileTree {
            return LogToFileTree(loggerName, logFileName, logsDir, fileSizeKB, historyLength, level)
        }
    }

    init {
        configure()
    }
}