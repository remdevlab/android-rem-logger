package org.remdev.timlog;

import timber.log.Timber;

public class SimpleLog implements Log {

    private final String tag;

    public SimpleLog(String tag) {
        this.tag = tag;
    }

    @Override
    public void i(String message, Object ... args) {
        Timber.tag(tag);
        Timber.i(message, args);
    }

    @Override
    public void i(Throwable e, String message, Object... args) {
        Timber.tag(tag);
        Timber.i(e, message, args);
    }

    @Override
    public void d(String message, Object... args) {
        Timber.tag(tag);
        Timber.d(message, args);
    }

    @Override
    public void d(Throwable e, String message, Object... args) {
        Timber.tag(tag);
        Timber.d(e, message, args);
    }

    @Override
    public void v(String message, Object... args) {
        Timber.tag(tag);
        Timber.v(message, args);
    }

    @Override
    public void v(Throwable e, String message, Object... args) {
        Timber.tag(tag);
        Timber.v(e, message, args);
    }

    @Override
    public void e(String message, Object... args) {
        Timber.tag(tag);
        Timber.e(message, args);
    }

    @Override
    public void e(Throwable e, String message, Object... args) {
        Timber.tag(tag);
        Timber.e(e, message, args);
    }

    @Override
    public void w(String message, Object... args) {
        Timber.tag(tag);
        Timber.w(message, args);
    }

    @Override
    public void w(Throwable e, String message, Object... args) {
        Timber.tag(tag);
        Timber.w(e, message, args);
    }
}
