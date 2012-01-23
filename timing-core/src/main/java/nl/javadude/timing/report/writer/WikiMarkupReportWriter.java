package nl.javadude.timing.report.writer;

import com.google.common.base.Joiner;
import nl.javadude.timing.report.Report;
import nl.javadude.timing.report.ReportLine;

import java.util.Date;

/**
 * @author Jeroen van Erp
 */
public class WikiMarkupReportWriter implements ReportWriter {

    private Report report;

    public WikiMarkupReportWriter(Report report) {
        this.report = report;
    }

    public String write() {
        final StringBuilder builder = new StringBuilder();
        builder.append("|| Statistics report (").append(new Date().toString()).append(") ||\n");
        builder.append("|| ").append(Joiner.on(" || ").join(report.columns())).append(" ||\n");
        for (ReportLine line : report.lines()) {
            builder.append("| ").append(Joiner.on(" | ").join(line.columns().values())).append(" |\n");
        }

        return builder.toString();
    }
}
