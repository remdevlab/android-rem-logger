package org.remdev.flog

import android.util.Log
import ch.qos.logback.classic.AsyncAppender
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

internal class LogToFileTree constructor(
    private var loggerName: String,
    private val logFileName: String,
    private val logsDir: String,
    private val fileSizeInBytes: Int,
    private val historyLength: Int,
    private val level: Level
) : Flog {
    lateinit var logger: Logger

    init {
        configure()
    }

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

        val asyncAppender = AsyncAppender()
        asyncAppender.context = loggerContext
        asyncAppender.name = "ASYNC-SINGLETON"
        asyncAppender.addAppender(rollingFileAppender)
        asyncAppender.start()

        // add the newly created appenders to the root logger;
        // qualify Logger to disambiguate from org.slf4j.Logger

        logger = LoggerFactory.getLogger(loggerName) as Logger
        logger.level = level
        logger.addAppender(asyncAppender)
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


}