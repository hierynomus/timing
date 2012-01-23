package nl.javadude.timing;

import nl.javadude.timing.collector.ActiveCollector;
import nl.javadude.timing.collector.BasicCollector;
import nl.javadude.timing.collector.TimerCollector;
import nl.javadude.timing.report.Report;
import nl.javadude.timing.report.ReportLine;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * I am the main glue point for Timers and Reports, offering access to the TimerCollectors.
 *
 * @author Jeroen van Erp
 */
public class TimerHolder {
    private static final ConcurrentMap<Timer.TimerKey, ConcurrentLinkedQueue<TimerCollector>> TIMERS = new ConcurrentHashMap<Timer.TimerKey, ConcurrentLinkedQueue<TimerCollector>>();

    private static final LinkedBlockingQueue<Timer.TimerResult> TO_BE_PROCESSED = new LinkedBlockingQueue<Timer.TimerResult>();

    /**
     * Start an asynchronous thread which handles all statistics updating, so that application logic is not impeded.
     */
    static {
        new Thread(new Runnable() {
            public void run() {
                try {
                    // The wheels on the bus go round and round...
                    // noinspection InfiniteLoopStatement
                    while(true) {
                        final Timer.TimerResult timer = TO_BE_PROCESSED.take();
                        final ConcurrentLinkedQueue<TimerCollector> linkedQueue = TIMERS.get(timer.getKey());
                        if (linkedQueue != null) {
                            // The queue could be null if the timers were cleared.
                            for (TimerCollector collector : linkedQueue) {
                                collector.collect(timer);
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    // The standard interrupt dance...
                    Thread.currentThread().interrupt();
                }
            }
        }, "TimerUpdaterThread").start();
    }

    static void register(Timer.TimerKey key, Timer timer) {
        if (TIMERS.get(key) == null || TIMERS.get(key).isEmpty()) {
            final ConcurrentLinkedQueue linkedQueue = new ConcurrentLinkedQueue();
            linkedQueue.add(new BasicCollector(key));
            linkedQueue.add(new ActiveCollector(key));
            TIMERS.put(key, linkedQueue);
//            TIMERS.put(key, new TimerIntervalBucketCollector(key));
        }

        for (TimerCollector collector : TIMERS.get(key)) {
            collector.register(timer);
        }
    }

    static void update(Timer.TimerResult timer) {
        try {
            TO_BE_PROCESSED.put(timer);
        } catch (InterruptedException e) {
            // The standard interrupt dance...
            Thread.currentThread().interrupt();
        }
    }

    public static Report report() {
        Report report = new Report();
        for (Timer.TimerKey key : TIMERS.keySet()) {
            final ReportLine reportLine = new ReportLine();
            reportLine.column("name", key.toString());
            for (TimerCollector collector : TIMERS.get(key)) {
                collector.addStatistics(reportLine);
            }
            report.addLine(reportLine);
        }

        return report;
    }

    public static void clearTimers() {
        TIMERS.clear();
    }
}
