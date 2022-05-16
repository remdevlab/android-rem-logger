package org.remdev.timlogtest.application

import android.app.Application
import android.os.Environment
import org.remdev.flog.Flog
import org.remdev.flog.FlogBuilder
import org.remdev.timlog.ConfigBasedLogToFileTree
import org.remdev.timlog.LogFactory
import org.remdev.timlog.LogToFileTree
import timber.log.Timber.DebugTree
import java.io.File

class TestLoggerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val configTree = ConfigBasedLogToFileTree()
        val logToFileTree = LogToFileTree.Builder()
            .logFileName("test-logs.log")
            .logsDir(Environment.getExternalStorageDirectory().absolutePath + File.separator + "logs")
            .historyLength(2)
            .build()
        LogFactory.configure(DebugTree(), configTree, logToFileTree)
        setupLoggers()
    }

    private fun setupLoggers() {
        logFile1 = FlogBuilder(this)
            .logger("NAME1")
            .logFileName("first")
            .logsDir(cacheDir.absolutePath)
            .build()
        logFile2 = FlogBuilder(this)
            .logger("NAME2")
            .logFileName("second")
            .logsDir(getLogsFolder())
            .build()
    }

    companion object {
        lateinit var logFile1: Flog
        lateinit var logFile2: Flog
    }

    fun getLogsFolder(): String {
        var path: String = cacheDir.absolutePath
        externalCacheDir?.let {
            path = it.absolutePath + File.separator + "LOGS"
        }
        return path
    }
}