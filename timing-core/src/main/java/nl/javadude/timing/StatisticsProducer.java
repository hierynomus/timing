package nl.javadude.timing;

/**
 * I can produce Statistics when I'm started and stopped.
 *
 * @see nl.javadude.timing.Timer
 */
public interface StatisticsProducer {

    /**
     * Start me.
     */
    void start();

    /**
     * Stop me.
     */
    void stop();
}
