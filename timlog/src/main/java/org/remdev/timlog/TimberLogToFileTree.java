package org.remdev.timlog;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.Charset;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.StatusPrinter;
import timber.log.Timber;

public class TimberLogToFileTree extends Timber.DebugTree {
    private static Logger mLogger = LoggerFactory.getLogger(TimberLogToFileTree.class);
    private static final String LOG_PREFIX = "ehail-sdk-log";
    private static final String LOG_DIR = "ehail-sdk/logs/";

    public TimberLogToFileTree(Context context) {
        File externalFilesDir = context.getExternalFilesDir(null);
        String state = Environment.getExternalStorageState();
        if (state == null || state.equals(Environment.MEDIA_MOUNTED) == false) {
            Log.w(TimberLogToFileTree.class.getName(), String.format("Could not log to file. External media state: %s", state));
            return;
        }
        File logsDirectory = new File(externalFilesDir + LOG_DIR);
        boolean fileOk = logsDirectory.exists() || logsDirectory.mkdirs();
        if (fileOk) {
            final String logDirectory = logsDirectory.getPath();
            configureLogger(logDirectory);
        } else {
            Log.w(TimberLogToFileTree.class.getName(), String.format("Could not log to file. Cannot access: %s", logsDirectory));
        }
    }

    private void configureLogger(String logDirectory) {
        // reset the default context (which may already have been initialized)
        // since we want to reconfigure it
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.reset();

        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<>();
        rollingFileAppender.setContext(loggerContext);
        rollingFileAppender.setAppend(true);
        rollingFileAppender.setFile(logDirectory + File.separator + LOG_PREFIX + ".log");

        SizeAndTimeBasedFNATP<ILoggingEvent> fileNamingPolicy = new SizeAndTimeBasedFNATP<>();
        fileNamingPolicy.setContext(loggerContext);
        fileNamingPolicy.setMaxFileSize("10MB");

        TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new TimeBasedRollingPolicy<>();
        rollingPolicy.setContext(loggerContext);
        rollingPolicy.setFileNamePattern(logDirectory + File.separator + LOG_PREFIX + ".%d{yyyy-MM-dd}.%i.log");
        rollingPolicy.setMaxHistory(5);
        rollingPolicy.setTimeBasedFileNamingAndTriggeringPolicy(fileNamingPolicy);
        rollingPolicy.setParent(rollingFileAppender);  // parent and context required!
        rollingPolicy.start();

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setCharset(Charset.forName("UTF-8"));
        encoder.setPattern("%date %level [%thread] %msg%n");
        encoder.start();

        rollingFileAppender.setRollingPolicy(rollingPolicy);
        rollingFileAppender.setEncoder(encoder);
        rollingFileAppender.start();

        // add the newly created appenders to the root logger;
        // qualify Logger to disambiguate from org.slf4j.Logger
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.DEBUG);
        root.addAppender(rollingFileAppender);

        // print any status messages (warnings, etc) encountered in logback config
        StatusPrinter.print(loggerContext);
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        if (priority == Log.VERBOSE) {
            return;
        }

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