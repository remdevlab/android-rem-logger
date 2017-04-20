package org.remdev.timlog;


import timber.log.Timber;

public final class LogFactory {

    private static final Object lock = new Object();
    private static LogFactory instance;

    public LogFactory(Timber.Tree primary) {
        Timber.plant(primary);
    }

    public static void configure(Timber.Tree first, Timber.Tree ... others) {
        synchronized (lock) {
            if (instance != null) {
                Timber.plant(first);
            } else {
                getInstance(first);
            }
        }
        if (others != null) {
            Timber.plant(others);
        }
    }

    public static void autoConfigure() {
        synchronized (lock) {
            if (instance != null) {
                throw new IllegalStateException("LogFactory is already configured!");
            }
        }
        configure(new ConsoleTree());
    }

    /**
     * Creates simple logger with class name as a tag
     */
    public static Log create(Class<?> cls) {
        return new SimpleLog(cls.getName());
    }

    private static LogFactory getInstance(Timber.Tree primary) {
        if (instance != null) {
            return instance;
        }
        synchronized (lock) {
            if (instance == null) {
                instance = new LogFactory(primary);
            }
        }
        return instance;
    }
}
