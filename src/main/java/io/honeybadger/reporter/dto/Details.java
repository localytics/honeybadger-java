package io.honeybadger.reporter.dto;

import org.slf4j.MDC;

import java.io.Serializable;
import java.util.*;

import static io.honeybadger.reporter.NoticeReporter.*;

/**
 * Class representing metadata and run-time state.
 * @author <a href="https://github.com/dekobon">Elijah Zupancic</a>
 * @since 1.0.9
 */
public class Details extends LinkedHashMap<String, LinkedHashMap<String, String>>
        implements Serializable {
    private static final long serialVersionUID = -6238693264237448645L;

    public Details() {
        addDefaultDetails();
    }

    protected void addDefaultDetails() {
        put("System Properties", systemProperties());
        put("MDC Properties", mdcProperties());
    }

    protected static LinkedHashMap<String, String> mdcProperties() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        @SuppressWarnings("unchecked")
        Map<String, String> mdc = MDC.getCopyOfContextMap();

        if (mdc != null) {
            for (Map.Entry<String, String> entry : mdc.entrySet()) {
                map.put(entry.getKey(), entry.getValue());
            }
        }

        return map;
    }

    protected static LinkedHashMap<String, String> systemProperties() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        Set<String> excludedSysProps = buildExcludedSysProps();

        for (Map.Entry<Object, Object> entry: System.getProperties().entrySet()) {
            // We skip all excluded properties
            if (excludedSysProps.contains(entry.getKey().toString())) {
                continue;
            }

            map.put(entry.getKey().toString(),
                    entry.getValue().toString());
        }

        return map;
    }

    protected static Set<String> buildExcludedSysProps() {
        String excluded = System.getProperty(HONEYBADGER_EXCLUDED_PROPS_SYS_PROP_KEY);
        HashSet<String> set = new HashSet<>();

        set.add(HONEYBADGER_API_KEY_SYS_PROP_KEY);
        set.add(HONEYBADGER_EXCLUDED_PROPS_SYS_PROP_KEY);
        set.add(HONEYBADGER_URL_SYS_PROP_KEY);

        if (excluded == null || excluded.isEmpty()) {
            return set;
        }

        Collections.addAll(set, excluded.split(","));

        return set;
    }
}
