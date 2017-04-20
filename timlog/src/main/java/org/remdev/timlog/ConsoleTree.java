package org.remdev.timlog;

import android.util.Log;

import timber.log.Timber;

public class ConsoleTree extends Timber.Tree {

    private static final String PATTERN = "[%s] %s: %s";

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {

        String level;
        switch (priority) {
            case Log.DEBUG:
                level = "DEBUG";
                break;
            case Log.WARN:
                level = "WARN";
                break;
            case Log.VERBOSE:
                level = "VERBOSE";
                break;
            case Log.ERROR:
                level = "ERROR";
                break;
            case Log.ASSERT:
                level = "ASSERT";
                break;
            default:
            case Log.INFO:
                level = "INFO";
                break;
        }

        if (tag == null) {
            tag = "Common";
        }
        String logMessage = String.format(PATTERN, level, tag, message);
        System.out.println(logMessage);
        if (t != null) {
            t.printStackTrace();
        }
    }
}
