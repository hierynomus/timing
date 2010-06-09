package nl.javadude.timing;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author Jeroen van Erp
 */
public class TimerTest {

    @Before
    public void init() {
        TimeUtils.freeze();
    }

    @After
    public void cleanUp() {
        TimeUtils.useSystemTime();
    }

    @Test
    public void shouldTimeCorrectlyStartStop() {
        Timer t = new Timer(defaultTimerKey());
        t.start();
        TimeUtils.elapseNanos(1000000);
        t.stop();
        assertThat(t.getResult().getDuration(), is(1000000L));
    }

    @Test
    public void exactDurationDoesNotViolateThreshold() {
        Timer t = new Timer(defaultTimerKey(), 100L);
        t.start();
        TimeUtils.elapseNanos(100L);
        t.stop();
        assertFalse(t.isThresholdViolated());
    }

    @Test
    public void shouldWarnThreshold() {
        Timer t = new Timer(defaultTimerKey(), 100L);
        t.start();
        TimeUtils.elapseNanos(101L);
        t.stop();
        assertTrue(t.isThresholdViolated());
    }
    
    @Test
    public void shouldBeNamedCorrectly() {
        assertThat(defaultTimerKey().toString(), is("test"));
    }

    @Test
    public void shouldTimerKeyBeIdem() {
        Timer.TimerKey timerKey = new Timer.TimerKey("a");
        assertTrue(timerKey.equals(new Timer.TimerKey("a")));
        assertFalse(timerKey.equals(new Timer.TimerKey("b")));
        assertFalse(timerKey.equals(null));
        assertFalse(timerKey.equals(new Object()));
        assertTrue(timerKey.equals(timerKey));
        assertTrue(timerKey.hashCode() == new Timer.TimerKey("a").hashCode());
        assertFalse(timerKey.hashCode() == new Timer.TimerKey("b").hashCode());
    }

    @Test(expected=IllegalStateException.class)
    public void cannotRestartStoppedTimer() {
        Timer t = new Timer(defaultTimerKey());
        try {
            t.start();
            t.stop();
            assertThat(t.getState(), is(Timer.State.STOPPED));
        } catch (Exception e) {
            fail();
        }
        t.start();
        fail();
    }

    @Test(expected=IllegalStateException.class)
    public void cannotStopNotStartedTimer() {
        new Timer(defaultTimerKey()).stop();
    }

    private Timer.TimerKey defaultTimerKey() {
        return new Timer.TimerKey("test");
    }
}
