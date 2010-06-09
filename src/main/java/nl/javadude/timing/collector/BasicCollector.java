package nl.javadude.timing.collector;

import nl.javadude.timing.TimeUtils;
import nl.javadude.timing.Timer;
import nl.javadude.timing.report.ReportLine;

/**
 * @author Jeroen van Erp
 */
public class BasicCollector implements TimerCollector {

    private long totalDuration;
    private long minimalDuration = Long.MAX_VALUE;
    private long maximumDuration = Long.MIN_VALUE;
    private long numberOfSamples;
    private final Timer.TimerKey key;

    public BasicCollector(Timer.TimerKey key) {
        this.key = key;
    }

    @Override
    public void register(Timer timer) {
    }

    @Override
    public void collect(Timer.TimerResult timer) {
        numberOfSamples++;
        long timerDuration = timer.getDuration();
        totalDuration += timerDuration;
        minimalDuration = minimalDuration > timerDuration ? timerDuration : minimalDuration;
        maximumDuration = maximumDuration < timerDuration ? timerDuration : maximumDuration;
    }

    long getMedianDuration() {
        return (minimalDuration + maximumDuration) / 2;
    }

    public Timer.TimerKey getKey() {
        return key;
    }

    long getAverageDuration() {
        if (numberOfSamples == 0) {
            return 0;
        }
        return totalDuration / numberOfSamples;
    }

    public void addStatistics(ReportLine line) {
        line.column("name", key.toString());
        line.column("samples", numberOfSamples);
        line.column("average", getAverageDuration() / TimeUtils.MILLI_NANO);
        line.column("total", totalDuration / TimeUtils.MILLI_NANO);
        line.column("minimal", minimalDuration / TimeUtils.MILLI_NANO);
        line.column("maximum", maximumDuration / TimeUtils.MILLI_NANO);
        line.column("median", getMedianDuration() / TimeUtils.MILLI_NANO);
    }
}
