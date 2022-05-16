package org.remdev.timlogtest.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.remdev.flog.Holder
import org.remdev.timlogtest.R
import org.remdev.timlogtest.application.TestLoggerApplication.Companion.logFile1
import org.remdev.timlogtest.application.TestLoggerApplication.Companion.logFile2

class TestLoggerActivity : AppCompatActivity() {


    //private val log = LogFactory.create(TestLoggerActivity::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        //log.i("onCreate at %d", System.currentTimeMillis())

        logFile1.log(Log.DEBUG, null, "onCreate at %d" + System.currentTimeMillis(), null)
        logFile2.log(Log.DEBUG, null, "onCreate at %d" + System.currentTimeMillis(), null)

    }

    override fun onResume() {
        super.onResume()
        // log.i("onResume at %d", System.currentTimeMillis())
        // log.e(Exception("TEST"), "test")
        logFile1.log(
            Log.DEBUG,
            null,
            "onCreate at %d" + System.currentTimeMillis(),
            Exception("TEST11111111111111")
        )
        logFile2.log(
            Log.DEBUG,
            null,
            "onCreate at %d" + System.currentTimeMillis(),
            Exception("TEST2222222222222")
        )
    }

}