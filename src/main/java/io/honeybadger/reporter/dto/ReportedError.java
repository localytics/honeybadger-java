package io.honeybadger.reporter.dto;

import java.io.Serializable;

/**
 * Class representing an error that is reported to the Honeybadger API.
 * @author <a href="https://github.com/dekobon">Elijah Zupancic</a>
 * @since 1.0.9
 */
public class ReportedError implements Serializable {
    private static final long serialVersionUID = 1661111694538362413L;

    private Notifier notifier = new Notifier();
    private ServerDetails server = new ServerDetails();
    private Details details = new Details();
    // This is defined as serializable so that it can use APIs that the
    // implementers may not have available like the Servlet API
    private Serializable request;
    private ErrorDetails error;

    public ReportedError() {
    }

    public Notifier getNotifier() {
        return notifier;
    }

    public ReportedError setNotifier(Notifier notifier) {
        this.notifier = notifier;
        return this;
    }

    public ErrorDetails getError() {
        return error;
    }

    public ReportedError setError(ErrorDetails error) {
        this.error = error;
        return this;
    }

    public ReportedError setServer(ServerDetails server) {
        this.server = server;
        return this;
    }

    public ServerDetails getServer() {
        return server;
    }

    public Details getDetails() {
        return details;
    }

    public ReportedError setDetails(Details details) {
        this.details = details;
        return this;
    }

    public Serializable getRequest() {
        return request;
    }

    public ReportedError setRequest(Serializable request) {
        this.request = request;
        return this;
    }
}
