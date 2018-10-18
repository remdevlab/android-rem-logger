package org.remdev.timlogtest.application;

import android.app.Application;
import android.os.Environment;

import org.remdev.timlog.ConfigBasedLogToFileTree;
import org.remdev.timlog.LogFactory;
import org.remdev.timlog.LogToFileTree;

import java.io.File;

import timber.log.Timber;

public class TestApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ConfigBasedLogToFileTree configTree = new ConfigBasedLogToFileTree();
        LogToFileTree logToFileTree = new LogToFileTree.Builder()
                .logFileName("test-logs.log")
                .logsDir(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "logs")
                .historyLength(2)
                .build();
        LogFactory.configure(new Timber.DebugTree(), configTree, logToFileTree);
    }
}
