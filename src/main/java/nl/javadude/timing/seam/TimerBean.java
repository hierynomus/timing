package nl.javadude.timing.seam;

import nl.javadude.timing.TimerHolder;
import nl.javadude.timing.report.Report;
import nl.javadude.timing.report.writer.HtmlMarkupReportWriter;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jeroen van Erp
 */
@Name("timerBean")
@Scope(ScopeType.APPLICATION)
public class TimerBean {
    private static final Logger log = LoggerFactory.getLogger(TimerBean.class);

    public String htmlReport() {
        log.info("Generating Timer log");
        final Report report = TimerHolder.report();
        return new HtmlMarkupReportWriter().writeReport(report);
    }
}
