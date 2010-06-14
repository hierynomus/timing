package nl.javadude.timing;

import nl.javadude.timing.report.Report;
import nl.javadude.timing.report.writer.WikiMarkupReportWriter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;

public class StatisticsFactory {

    private StatisticsFactory() {
    }

    static {
        // Register a shutdown hook to print the report at VM shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            private final Logger log = LoggerFactory.getLogger("TimerShutdownHook");

            @Override
            public void run() {
                Report report = TimerHolder.report();
                log.error(new WikiMarkupReportWriter(report).write());
            }
        }, "TimerShutdownHook"));
    }

    public static Timer start(Class<?> clazz, String name) {
        return start(clazz, name, null);
    }

    /**
     * Start a new StatisticsProducer.
     * @param clazz
     * @param name
     * @param thresholdInMillis
     * @return
     */
    public static Timer start(Class<?> clazz, String name, Long thresholdInMillis) {
        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(clazz.getName());
        if (!StringUtils.isEmpty(name)) {
            nameBuilder.append(".").append(name);
        }

        return start(nameBuilder.toString(), thresholdInMillis);
    }

    private static Timer createTimer(String name, Long thresholdInMillis) {
        Timer.TimerKey key = new Timer.TimerKey(name);
        if (thresholdInMillis != null) {
            return new Timer(key, thresholdInMillis * TimeUtils.MILLI_NANO);
        } else {
            return new Timer(key);
        }
    }

    public static Timer start(String name) {
        return start(name, null);
    }

    public static Timer start(String name, Long thresholdInMillis) {
        final Timer timer = createTimer(name, thresholdInMillis);
        timer.start();
        return timer;
    }
}
