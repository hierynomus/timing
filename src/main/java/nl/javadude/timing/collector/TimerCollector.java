package nl.javadude.timing.collector;

import nl.javadude.timing.Timer;
import nl.javadude.timing.report.ReportLine;

import java.util.Map;

/**
 * A note on Thread-safety: Though a TimerCollector can be accessed from multiple threads at the same time, a single timer
 * should never be shared between threads.
 *
 * @author Jeroen van Erp
 */
public interface TimerCollector {
    void register(Timer timer);

    void collect(Timer.TimerResult timer);

    Timer.TimerKey getKey();

    void addStatistics(ReportLine line);
}
