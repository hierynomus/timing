package nl.javadude.timing.collector;

import nl.javadude.timing.Timer;
import nl.javadude.timing.report.ReportLine;

/**
 *
 *
 * @author Jeroen van Erp
 */
public class TimerIntervalBucketCollector implements TimerCollector {
    private final Timer.TimerKey key;

    public TimerIntervalBucketCollector(Timer.TimerKey key) {
        this.key = key;
    }

    @Override
    public void register(Timer timer) {
    }

    @Override
    public void collect(Timer.TimerResult timer) {
    }

    @Override
    public Timer.TimerKey getKey() {
        return key;
    }

    @Override
    public void addStatistics(ReportLine line) {

    }

    class Bucket {
        private final long minInterval;
        private final long maxInterval;
        
        Bucket(long minInterval, long maxInterval) {
            this.minInterval = minInterval;
            this.maxInterval = maxInterval;
        }

        boolean inBucket(Timer.TimerResult timer) {
            final long duration = timer.getDuration();
            return minInterval <= duration && duration < maxInterval;
        }
    }
}
