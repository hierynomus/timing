package nl.javadude.timing.report;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Jeroen van Erp
 */
public class ReportLine {

    private Map<String, String> columns = new LinkedHashMap<String, String>();

    public void column(String name, String value) {
        columns.put(name, value);
    }

    public void column(String name, long value) {
        column(name, Long.toString(value));
    }

    public Map<String, String> columns() {
        return columns;
    }
}
