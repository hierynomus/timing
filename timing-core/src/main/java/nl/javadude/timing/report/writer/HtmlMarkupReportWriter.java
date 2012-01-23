package nl.javadude.timing.report.writer;

import com.google.common.base.Joiner;
import nl.javadude.timing.report.Report;
import nl.javadude.timing.report.ReportLine;

import java.util.Date;
import java.util.Set;

/**
 * @author Jeroen van Erp
 */
public class HtmlMarkupReportWriter implements ReportWriter {

    private Report report;

    public HtmlMarkupReportWriter(Report report) {
        this.report = report;
    }

    public String write() {
        final StringBuilder builder = new StringBuilder();
        final Set<String> columns = report.columns();
        builder.append("<table>");
        builder.append("<tr><th colspan='").append(columns.size()).append("'>Statistics report (").append(new Date().toString()).append("</th></tr>");
        builder.append("<tr><th>").append(Joiner.on("</th><th>").join(columns)).append("</th></tr>");
        for (ReportLine line : report.lines()) {
            builder.append("<tr><td>").append(Joiner.on("</td><td>").join(line.columns().values())).append("</td></tr>");
        }
        builder.append("</table>");

        return builder.toString();
    }
}