package nl.javadude.timing.report.writer;

import nl.javadude.timing.report.Report;
import nl.javadude.timing.report.ReportLine;
import org.apache.commons.lang.StringUtils;

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
        builder.append("|| ").append(StringUtils.join(report.columns(), " || ")).append(" ||\n");
        for (ReportLine line : report.lines()) {
            builder.append("| ").append(StringUtils.join(line.columns().values(), " | ")).append(" |\n");
        }

        return builder.toString();
    }
}
