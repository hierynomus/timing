package nl.javadude.timing.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Jeroen van Erp
 */
public class Report {

    List<ReportLine> lines = new ArrayList<ReportLine>();

    public void addLine(ReportLine line) {
        lines.add(line);
    }

    public List<ReportLine> lines() {
        return lines;
    }

    public Set<String> columns() {
        if (lines.size() == 0) {
            return Collections.EMPTY_SET;
        }
        return lines.get(0).columns().keySet();
    }
}
