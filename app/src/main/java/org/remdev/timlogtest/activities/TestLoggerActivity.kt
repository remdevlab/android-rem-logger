package org.remdev.timlogtest.activities

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import fr.bipi.tressence.file.FileLoggerTree
import org.remdev.timlog.LogFactory
import org.remdev.timlog.LogToFileTree
import org.remdev.timlogtest.R
import org.remdev.timlogtest.application.TestLoggerApplication
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.util.concurrent.TimeUnit

class TestLoggerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val btnSendLogs = findViewById<TextView>(R.id.btnWriteToLogs)
        btnSendLogs.setOnClickListener {
            writeThreeLogs()
        }
        val btnSendLogsNew = findViewById<TextView>(R.id.btnWriteNewLogToLogs)
        btnSendLogsNew.setOnClickListener {
            testOneTime()
        }
        val btnExternalLogger = findViewById<TextView>(R.id.btnExternal)
        btnExternalLogger.setOnClickListener {
            testLogback()
        }
    }

    private fun testOneTime() {
        val LOG = LogFactory.create("TEST-TAG")
        Timber.w("3+ ++")
        LOG.w("TEST")
        Timber.d("============ diff =  ms")
    }

    private fun writeThreeLogs() {
        val logToFileTree = LogToFileTree.Builder()
            .logFileName("test-logs.log")
            .logsDir(TestLoggerApplication.instance.getLogsFolder())
            .historyLength(20)
            .fileSizeMB(20)
            .build()
        LogFactory.configure(DebugTree(), logToFileTree)

        val LOG = LogFactory.create("TEST-TAG")
        Timber.d("3+ ++")
        Timber.d("3+ ++")
        Timber.d("3+ ++")
        Timber.d("3+ ++")
        Timber.d("3+ ++")
        LOG.d("TEST")
        var start = System.nanoTime()
        for (it in 0..1500) {
            Timber.w("3+ ++ $it")
        }
        var diff = start - System.nanoTime()
        println("DONE")
        Log.w("TEST", "============ diff = ${TimeUnit.NANOSECONDS.toMillis(diff)} ms")
        Timber.d("============ diff = ${TimeUnit.NANOSECONDS.toMillis(diff)} ms")
        Timber.d("debug")
        Timber.v("verbose")
        Timber.i("info")
        Timber.w("warn")
        Timber.e("error")
    }

    private fun testLogback() {
        val tree: Timber.Tree = FileLoggerTree.Builder()
            .withFileName("newfile%g.log")
            .withDirName(TestLoggerApplication.instance.getLogsFolder())
            .withSizeLimit(20000)
            .withFileLimit(3)
            .withMinPriority(Log.DEBUG)
            .appendToFile(true)
            .build()
        Timber.plant(tree)
        Timber.w("3+ ++")
        var start = System.nanoTime()
        for (it in 0..1500) {
            Timber.w("3+ ++ $it")
        }
        var diff = start - System.nanoTime()
        Log.w("TEST", "============ diff = ${TimeUnit.NANOSECONDS.toMillis(diff)} ms")
    }

}