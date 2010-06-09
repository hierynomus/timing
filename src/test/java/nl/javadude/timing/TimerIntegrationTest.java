package nl.javadude.timing;

import nl.javadude.timing.collector.TimerCollector;
import nl.javadude.timing.report.Report;
import nl.javadude.timing.report.writer.HtmlMarkupReportWriter;
import nl.javadude.timing.report.writer.WikiMarkupReportWriter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @author Jeroen van Erp
 */
public class TimerIntegrationTest {

    @Before
    public void freeze() {
        TimeUtils.freeze();
    }

    @After
    public void defrost() {
        TimeUtils.useSystemTime();
    }

    @Test
    public void shouldRegisterAndUpdateTimers() {
        final Timer timer = StatisticsFactory.start(TimerIntegrationTest.class, "1");
        TimeUtils.elapseMillis(100);
        timer.stop();
        final Report report = TimerHolder.report();
        assertThat(report.lines().size(), is(1));
    }

    @Test
    public void shouldStartLotOfTimers() throws InterruptedException {
        TimeUtils.useSystemTime();
        final CountDownLatch countDownLatch = new CountDownLatch(50);
        for (int i = 0; i < 50; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Timer timer = StatisticsFactory.start(TimerIntegrationTest.class, "test");
                    try {
                        Thread.sleep(new Random().nextInt(100));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        timer.stop();
                    }

                    countDownLatch.countDown();
                }
            }).start();
        }

        countDownLatch.await();
        final Report report = TimerHolder.report();
        LoggerFactory.getLogger(TimerIntegrationTest.class).error(new WikiMarkupReportWriter().writeReport(report));
        LoggerFactory.getLogger(TimerIntegrationTest.class).error(new HtmlMarkupReportWriter().writeReport(report));
    }
}
