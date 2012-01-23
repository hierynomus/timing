package nl.javadude.timing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Timer is a one-shot StatisticsProducer. It cannot be re-used.
 * The Timer will record the elapsed time between a call to {@link #start()} and {@link #stop()} with a granularity of nanoseconds.
 *
 * A Timer is not Threadsafe and should not be shared among threads!
 *
 * @author Jeroen van Erp
 */
public class Timer implements StatisticsProducer {
    private static final Logger log = LoggerFactory.getLogger(Timer.class);
    private boolean thresholdViolated;
    private TimerResult duration;

    public enum State {
        IDLE, RUNNING, STOPPED
    }

    private State state;
    private long currentStart;
    private final TimerKey key;
    private Long threshold;

    Timer(TimerKey key) {
        this(key, null);
    }

    Timer(TimerKey key, Long threshold) {
        this.key = key;
        this.threshold = threshold;
        this.state = State.IDLE;
        TimerHolder.register(key, this);
    }

    public void start() {
        if (this.state != State.IDLE) {
            throw new IllegalStateException(String.format("Cannot start an already used timer %s", key));
        }
        currentStart = TimeUtils.nanoTime();
        this.state = State.RUNNING;
    }

    public void stop() {
        if (this.state != State.RUNNING) {
            throw new IllegalStateException(String.format("Cannot stop STOPPED timer %s", key));
        }
        this.state = State.STOPPED;
        duration = new TimerResult(key, TimeUtils.nanoTime() - currentStart);
        TimerHolder.update(duration);
        if (threshold != null && duration.getDuration() > threshold) {
            thresholdViolated = true;
            log.error("Call to {} took more than threshold --> {} nanos", key, duration);
        }
    }

    TimerResult getResult() {
        return duration;
    }

    public TimerKey getKey() {
        return key;
    }

    State getState() {
        return state;
    }

    public boolean isThresholdViolated() {
        return thresholdViolated;
    }

    @Override
    public String toString() {
        return "Timer[name=" + key + ", state=" + state + ", duration=" + duration + "]";
    }

    public static class TimerResult {
        private long duration;
        private TimerKey key;

        TimerResult(TimerKey key, long timer) {
            this.key = key;
            duration = timer;
        }

        public long getDuration() {
            return duration;
        }

        public TimerKey getKey() {
            return key;
        }

        @Override
        public String toString() {
            return Long.toString(duration);
        }
    }

    public static class TimerKey {
        private String name;

        public TimerKey(String name) {
            if (name == null) {
                throw new IllegalArgumentException("TimerKey does not accept null");
            }
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TimerKey timerKey = (TimerKey) o;
            return name.equals(timerKey.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
