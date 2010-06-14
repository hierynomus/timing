package nl.javadude.timing.template;

import nl.javadude.timing.StatisticsFactory;
import nl.javadude.timing.Timer;

/**
 * A template that wraps a call in a timer start/stop cycle.
 *
 * @author Jeroen van Erp
 */
public class TimerTemplate {

    public void time(String name, TimerCallback callback) {
        final Timer timer = StatisticsFactory.start(name);
        try {
            callback.doInTimer();
        } finally {
            timer.stop();
        }
    }

    public void time(Class<?> clazz, String name, TimerCallback callback) {
        final Timer timer = StatisticsFactory.start(clazz, name);
        try {
            callback.doInTimer();
        } finally {
            timer.stop();
        }
    }

    public static interface TimerCallback {
        public void doInTimer();
    }
}
