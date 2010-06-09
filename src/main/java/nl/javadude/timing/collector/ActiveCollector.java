package nl.javadude.timing.collector;

import nl.javadude.timing.Timer;
import nl.javadude.timing.report.ReportLine;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Jeroen van Erp
 */
public class ActiveCollector implements TimerCollector {
    private Timer.TimerKey key;
    private AtomicInteger numActive = new AtomicInteger(0);
    private AtomicInteger maxActive = new AtomicInteger(0);

    public ActiveCollector(Timer.TimerKey key) {
        this.key = key;
    }

    @Override
    public void register(Timer timer) {
        int active = numActive.incrementAndGet();
        if (maxActive.get() < active) {
            maxActive.set(active);
        }
    }

    @Override
    public void collect(Timer.TimerResult timer) {
        numActive.decrementAndGet();
    }

    @Override
    public Timer.TimerKey getKey() {
        return key;
    }

    @Override
    public void addStatistics(ReportLine line) {
        line.column("num. active", numActive.toString());
        line.column("max. active", maxActive.toString());
    }
}
