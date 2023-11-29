package org.remdev.timlogtest

import android.util.Log
import fr.bipi.tressence.file.FileLoggerTree
import org.remdev.timlog.LogFactory
import org.remdev.timlogtest.application.TestLoggerApplication
import timber.log.Timber
import java.util.Date


object TestLog {
    private val LOG = LogFactory.create("TEST-TAG")

    init {
        val t: Timber.Tree = FileLoggerTree.Builder()
            .withFileName("file%g.log")
            .withDirName(TestLoggerApplication.instance.getLogsFolder())
            .withSizeLimit(20000)
            .withFileLimit(3)
            .withMinPriority(Log.DEBUG)
            .appendToFile(true)
            .build()
        Timber.plant(t)
    }

    fun log(msg: String) {
        LOG.d(msg)
    }

    fun logNI(msg: String) {
        LogFactory.create("" + Date().time).d(msg)
    }

    fun logThree(msg: String) {
       Timber.d(msg)
    }
}