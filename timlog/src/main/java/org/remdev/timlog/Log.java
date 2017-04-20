package org.remdev.timlog;


public interface Log {

    void i(String message, Object... args);

    void i(Throwable e, String message, Object... args);

    void d(String message, Object... args);

    void d(Throwable e, String message, Object... args);

    void v(String message, Object... args);

    void v(Throwable e, String message, Object... args);

    void e(String message, Object... args);

    void e(Throwable e, String message, Object... args);

    void w(String message, Object... args);

    void w(Throwable e, String message, Object... args);
}
