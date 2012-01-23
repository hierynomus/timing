package nl.javadude.timing;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * I redirect calls to me to System, unless I retain a staticTime. This is useful for testing.
 *
 * @author Jeroen van Erp
 */
public class TimeUtils {

    private static final AtomicLong staticTime = new AtomicLong(0);
    private static final AtomicBoolean useSystem = new AtomicBoolean(true);
    public static final int MILLI_NANO = 1000000;

    private TimeUtils() {
        // Do not instantiate!
    }

    /**
     * Use System time.
     */
    static void useSystemTime() {
        useSystem.set(true);
        staticTime.set(0);
    }

    /**
     * Keep the value of now as a return value.
     */
    static void freeze() {
        useSystem.set(false);
        staticTime.set(System.nanoTime());
    }

    /**
     * Elapse a number of nanoseconds.
     * @param nanos The amount to elapse
     */
    static void elapseNanos(long nanos) {
        staticTime.addAndGet(nanos);
    }

    /**
     * Elapse a number of milliseconds.
     * @param millis The amount to elapse
     */
    static void elapseMillis(long millis) {
        staticTime.addAndGet(millis * MILLI_NANO);
    }

    /**
     * Return the time in milliseconds.
     * @return the time in milliseconds
     */
    static long currentTimeMillis() {
        return useSystem.get() ? System.currentTimeMillis() : (staticTime.get() / MILLI_NANO);
    }

    /**
     * Return the time in nanoseconds.
     * @return the time in nanoseconds
     */
    static long nanoTime() {
        return useSystem.get() ? System.nanoTime() : staticTime.get();
    }
}
