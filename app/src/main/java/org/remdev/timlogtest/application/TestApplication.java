package org.remdev.timlogtest.application;

import android.app.Application;
import android.os.Environment;

import org.remdev.timlog.LogFactory;
import org.remdev.timlog.LogToFileTree;

import timber.log.Timber;

public class TestApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LogToFileTree logToFileTree = new LogToFileTree.Builder()
                .logFileName("test-logs.log")
                .logsDir(Environment.getExternalStorageDirectory().getAbsolutePath() + "logs")
                .fileSizeMB(2)
                .build();
        LogFactory.configure(new Timber.DebugTree(), logToFileTree);
    }
}
