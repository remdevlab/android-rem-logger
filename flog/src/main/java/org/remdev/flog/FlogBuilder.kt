package org.remdev.flog

import android.content.Context
import ch.qos.logback.classic.Level

/**
 *  Created by Alexandr Salin on 16.05.22
 */
class FlogBuilder(private val context: Context) {
    private var loggerName = "undefined"
    private var logFileName = "flog.txt"
    private var logsDir = context.applicationContext.cacheDir.absolutePath
    private var fileSizeKB = 0
    private var historyLength = 10
    private var level = Level.ALL

    fun logger(name: String): FlogBuilder {
        this.loggerName = name
        return this
    }

    fun logFileName(logFileName: String): FlogBuilder {
        this.logFileName = logFileName
        return this
    }

    fun logsDir(logsDir: String): FlogBuilder {
        this.logsDir = logsDir
        return this
    }

    fun fileSizeKB(fileSizeKB: Int): FlogBuilder {
        require(fileSizeKB > 0) { "File size should be greater than 0" }
        this.fileSizeKB = fileSizeKB * 1024
        return this
    }

    fun fileSizeMB(fileSizeMB: Int): FlogBuilder {
        require(fileSizeMB > 0) { "File size should be greater than 0" }
        fileSizeKB = fileSizeMB * 1024 * 1024
        return this
    }

    fun historyLength(historyLength: Int): FlogBuilder {
        this.historyLength = historyLength
        return this
    }

    fun level(level: Level): FlogBuilder {
        this.level = level
        return this
    }

    fun build(): Flog {
        return LogToFileTree(loggerName, logFileName, logsDir, fileSizeKB, historyLength, level)
    }
}