package org.remdev.timlog;

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
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.StatusPrinter;
import timber.log.Timber;

public class LogToFileTree extends Timber.DebugTree {
    private static Logger mLogger = LoggerFactory.getLogger(LogToFileTree.class);
    private final String logFileName;
    private final String logsDir;
    private final int fileSizeInBytes;
    private final int historyLength;
    private final Level level;

    public LogToFileTree(String logFileName, String logsDir, int fileSizeInBytes, int historyLength, Level level) {
        this.logFileName = logFileName;
        this.logsDir = logsDir;
        this.fileSizeInBytes = fileSizeInBytes;
        this.historyLength = historyLength;
        this.level = level;

        configure();
    }

    private void configure() {
        String externalFilesDir = this.logsDir.endsWith(File.separator) ? this.logsDir : this.logsDir + File.separator;
        File logsDirectory = new File(externalFilesDir);
        boolean fileOk = logsDirectory.exists() || logsDirectory.mkdirs();
        if (fileOk) {
            final String logDirectory = logsDirectory.getPath();
            configureLogger(logDirectory);
        } else {
            System.out.println(String.format("Could not log to file. Cannot access: %s", logsDirectory));
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
        rollingFileAppender.setFile(logDirectory + File.separator + logFileName + ".log");

        SizeAndTimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new SizeAndTimeBasedRollingPolicy<>();
        rollingPolicy.setContext(loggerContext);
        rollingPolicy.setTotalSizeCap(new FileSize(fileSizeInBytes * historyLength + 1));
        rollingPolicy.setMaxFileSize(new FileSize(fileSizeInBytes));
        rollingPolicy.setMaxHistory(historyLength);
        String fileNamePatternStr = logDirectory + File.separator + logFileName + ".%d{yyyy-MM-dd,UTC}.%i.log";
        rollingPolicy.setFileNamePattern(fileNamePatternStr);
        rollingPolicy.setParent(rollingFileAppender);  // parent and context required!
        rollingPolicy.setCleanHistoryOnStart(true);
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
        root.setLevel(level);
        root.addAppender(rollingFileAppender);

        // print any status messages (warnings, etc) encountered in logback config
        StatusPrinter.print(loggerContext);
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

    public static class Builder {
        private String logFileName = "app-log.txt";
        private String logsDir = "";
        private int fileSizeKB = 0;
        private int historyLength = 10;
        private Level level = Level.ALL;

        public Builder logFileName(String logFileName) {
            this.logFileName = logFileName;
            return this;
        }

        public Builder logsDir(String logsDir) {
            this.logsDir = logsDir;
            return this;
        }

        public Builder fileSizeKB(int fileSizeKB) {
            if (fileSizeKB <= 0) {
                throw new IllegalArgumentException("File size should be greater than 0");
            }
            this.fileSizeKB = fileSizeKB * 1024;
            return this;
        }

        public Builder fileSizeMB(int fileSizeMB) {
            if (fileSizeMB <= 0) {
                throw new IllegalArgumentException("File size should be greater than 0");
            }
            this.fileSizeKB = fileSizeMB * 1024 * 1024;
            return this;
        }

        public Builder historyLength(int historyLength) {
            this.historyLength = historyLength;
            return this;
        }

        public Builder level(Level level) {
            this.level = level;
            return this;
        }

        public LogToFileTree build() {
            return new LogToFileTree(logFileName, logsDir, fileSizeKB, historyLength, level);
        }
    }
}