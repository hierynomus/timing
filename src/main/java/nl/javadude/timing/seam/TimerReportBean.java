package nl.javadude.timing.seam;

import nl.javadude.timing.TimerHolder;
import nl.javadude.timing.report.Report;
import nl.javadude.timing.report.writer.HtmlMarkupReportWriter;
import nl.javadude.timing.report.writer.WikiMarkupReportWriter;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jeroen van Erp
 */
@Name("timerReportBean")
@Scope(ScopeType.APPLICATION)
public class TimerReportBean {
    private static final Logger log = LoggerFactory.getLogger(TimerReportBean.class);

    public String report() {
        final Report report = TimerHolder.report();
        return new HtmlMarkupReportWriter(report).write();
    }

    public void clear() {
        log.info("Cleaning Timer statistics");
        log.info("Current report \r\n {}", report());
        TimerHolder.clearTimers();
    }
}
