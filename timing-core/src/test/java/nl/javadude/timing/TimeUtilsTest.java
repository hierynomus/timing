package nl.javadude.timing;

import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
/**
 * @author Jeroen van Erp
 */
public class TimeUtilsTest {

    @After
    public void unfreeze() {
        TimeUtils.useSystemTime();
    }

    @Test
    public void shouldFreezeNanoTime() throws Exception {
        TimeUtils.freeze();
        long nanos = TimeUtils.nanoTime();
        Thread.sleep(10);
        long nanos2 = TimeUtils.nanoTime();
        assertThat(nanos, is(nanos2));
    }

    @Test
    public void shouldFreezeMilliTime() throws Exception {
        TimeUtils.freeze();
        long millis = TimeUtils.currentTimeMillis();
        Thread.sleep(10);
        long millis2 = TimeUtils.currentTimeMillis();
        assertThat(millis, is(millis2));
    }

    @Test
    public void shouldElapseMilliTime() throws Exception {
        TimeUtils.freeze();
        long millis = TimeUtils.currentTimeMillis();
        TimeUtils.elapseMillis(10);
        long millis2 = TimeUtils.currentTimeMillis();
        assertThat(millis2, is(millis + 10));
    }

    @Test
    public void shouldElapseNanoTime() throws Exception {
        TimeUtils.freeze();
        long nanos = TimeUtils.nanoTime();
        TimeUtils.elapseNanos(10);
        long nanos2 = TimeUtils.nanoTime();
        assertThat(nanos2, is(nanos + 10));
    }

    @Test
    public void shouldBeCorrectOnMillisAndNanos() throws Exception {
        TimeUtils.freeze();
        long nanos = TimeUtils.nanoTime();
        long millis = TimeUtils.currentTimeMillis();
        assertThat(nanos / TimeUtils.MILLI_NANO, is(millis));
    }

    @Test
    public void nanosElapseWhenNotFrozen() throws InterruptedException {
        long nanos = TimeUtils.nanoTime();
        Thread.sleep(10);
        assertTrue(TimeUtils.nanoTime() - nanos >= 10000000);
    }

    @Test
    public void millisElapseWhenNotFrozen() throws InterruptedException {
        long millis = TimeUtils.currentTimeMillis();
        Thread.sleep(10);
        assertTrue(TimeUtils.currentTimeMillis() - millis >= 10);
    }
}
