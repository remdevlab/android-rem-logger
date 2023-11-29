import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import fr.bipi.tressence.file.FileLoggerTree
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.remdev.timlog.LogFactory
import org.remdev.timlog.LogToFileTree
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class LoggerTest {

    fun getLogsFolder(): String {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        var path: String = context.cacheDir.absolutePath
     /*   context.externalCacheDir?.let {
            path = it.absolutePath + File.separator + "LOGS"
        }*/
        return path
    }

    @Test(timeout = 50000)
    fun test() {
        val logToFileTree = LogToFileTree.Builder()
            .logFileName("test-logs.log")
            .logsDir(getLogsFolder())
            .historyLength(2)
            .build()
        LogFactory.configure(Timber.DebugTree(), logToFileTree)
        val LOG = LogFactory.create("TEST-TAG")
        Timber.d("3+ ++")
        LOG.d("TEST")
        var start = System.nanoTime()
        repeat(150000){
            Timber.d("3+ ++ $it")
        }
        var diff = start - System.nanoTime()
        println("DONE")
        Log.w("TEST", "============ diff = ${TimeUnit.NANOSECONDS.toMillis(diff)} ms")
        Timber.d("============ diff = ${TimeUnit.NANOSECONDS.toMillis(diff)} ms")
        Assert.assertTrue(diff> -100)
    }

    @Test(timeout = 50000)
    fun testThreesome() {
        val tree: Timber.Tree = FileLoggerTree.Builder()
            .withFileName("file%g.log")
            .withDirName(getLogsFolder())
            .withSizeLimit(20000)
            .withFileLimit(3)
            .withMinPriority(Log.DEBUG)
            .appendToFile(true)
            .build()
        Timber.plant(tree)
        Timber.d("3+ ++")
        var start = System.nanoTime()
        repeat(150000) {
            Timber.d("3+ ++ $it")
        }
        var diff = start - System.nanoTime()
        Log.w("TEST", "============ diff = ${TimeUnit.NANOSECONDS.toMillis(diff)} ms")
        Assert.assertTrue(diff> -100)
    }

}