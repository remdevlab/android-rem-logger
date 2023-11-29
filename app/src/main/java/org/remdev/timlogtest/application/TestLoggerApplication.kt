package org.remdev.timlogtest.application

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import java.io.File


class TestLoggerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        )
    }

    companion object {
        lateinit var instance: TestLoggerApplication
    }

    fun getLogsFolder(): String {
        var path: String = cacheDir.absolutePath
        externalCacheDir?.let {
            path = it.absolutePath + File.separator + "LOGS"
        }
        return path
    }
}