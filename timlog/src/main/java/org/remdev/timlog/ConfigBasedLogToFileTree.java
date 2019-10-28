package org.remdev.timlog;

import android.util.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import timber.log.Timber;

/**
 * Uses logback.xml configuration file from assets folder
 */
public class ConfigBasedLogToFileTree extends Timber.DebugTree {
    private static Logger mLogger = LoggerFactory.getLogger(ConfigBasedLogToFileTree.class);

    public ConfigBasedLogToFileTree() {
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        String logMessage = String.format("%s: %s", tag, message);
        switch (priority) {
            case Log.DEBUG:
                mLogger.debug(logMessage, t);
                break;
            case Log.INFO:
                mLogger.info(logMessage, t);
                break;
            case Log.WARN:
                mLogger.warn(logMessage, t);
                break;
            case Log.ERROR:
                mLogger.error(logMessage, t);
                break;
        }
    }
}