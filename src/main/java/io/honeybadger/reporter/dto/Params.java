package io.honeybadger.reporter.dto;

import java.io.Serializable;
import java.util.*;

import static io.honeybadger.reporter.NoticeReporter.*;

/**
 * Class representing parameters requested when an exception occurred.
 * @author <a href="https://github.com/dekobon">Elijah Zupancic</a>
 * @since 1.0.9
 */
public class Params extends LinkedHashMap<String, String>
        implements Serializable {
    private static final long serialVersionUID = -5633548926144410598L;
    private final Set<String> excludedValues;

    public Params() {
        this.excludedValues = buildExcludedProps();
    }

    protected static Set<String> buildExcludedProps() {
        String excluded = System.getProperty(HONEYBADGER_EXCLUDED_PARAMS_SYS_PROP_KEY);
        HashSet<String> set = new HashSet<>();

        if (excluded == null || excluded.isEmpty()) {
            return set;
        }

        Collections.addAll(set, excluded.split(","));

        return set;
    }

    /**
     * Converts multiple HTTP parameters into a CSV format.
     * @param strings parameters to convert
     * @return CSV of params, otherwise empty string
     */
    static String csv(String[] strings) {
        if (strings == null || strings.length == 0) return "";
        if (strings.length == 1) return strings[0];

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < strings.length; i++) {
            builder.append(strings[i]);
            if (i < strings.length - 1) builder.append(", ");
        }

        return builder.toString();
    }

    @Override
    public String put(String key, String value) {
        if (excludedValues.contains(key)) {
            return null;
        }

        return super.put(key, value);
    }
}
