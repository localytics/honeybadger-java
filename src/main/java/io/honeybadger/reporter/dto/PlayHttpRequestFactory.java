package io.honeybadger.reporter.dto;

import org.apache.http.HttpHeaders;
import play.mvc.Http;

import java.util.Iterator;

import static io.honeybadger.reporter.dto.RequestParsingUtils.parseParamsFromMap;


/**
 * Factory class that creates a {@link Request} based on a
 * {@link play.mvc.Http.Request}.
 *
 * @author <a href="https://github.com/dekobon">Elijah Zupancic</a>
 * @since 1.0.9
 */
public class PlayHttpRequestFactory {
    public static Request create(Http.Request httpRequest) {
        Context context = createContext(httpRequest);
        String url = getFullURL(httpRequest);
        Params params = createParams(httpRequest);
        Session session = createSession(httpRequest);
        CgiData cgi_data = createCgiData(httpRequest);

        return new Request(context, url, params, session, cgi_data);
    }

    protected static Context createContext(Http.Request httpRequest) {
        Context context = new Context();

        String username =  httpRequest.username();

        if (username != null) {
            context.put("user_name", username);
        }

        return context;
    }

    protected static String getFullURL(Http.Request httpRequest) {
        return httpRequest.uri();
    }

    protected static Params createParams(Http.Request httpRequest) {
        Http.RequestBody body = httpRequest.body();

        if (body == null) return new Params();

        return parseParamsFromMap(body.asFormUrlEncoded());
    }

    protected static Session createSession(Http.Request httpRequest) {
        final Session session = new Session();

        // We don't support Play sessions or flash scopes yet
        // Please write me if you need me.

        return session;
    }

    protected static CgiData createCgiData(Http.Request httpRequest) {
        final CgiData cgiData = new CgiData();

        cgiData.put("REQUEST_METHOD", httpRequest.method());
        cgiData.put("HTTP_ACCEPT", httpRequest.getHeader(HttpHeaders.ACCEPT));
        cgiData.put("HTTP_USER_AGENT", httpRequest.getHeader(HttpHeaders.USER_AGENT));
        cgiData.put("HTTP_ACCEPT_ENCODING", httpRequest.getHeader(HttpHeaders.ACCEPT_ENCODING));
        cgiData.put("HTTP_ACCEPT_LANGUAGE", httpRequest.getHeader(HttpHeaders.ACCEPT_LANGUAGE));
        cgiData.put("HTTP_ACCEPT_CHARSET", httpRequest.getHeader(HttpHeaders.ACCEPT_CHARSET));
        cgiData.put("HTTP_COOKIE", parseCookies(httpRequest));
        cgiData.put("SERVER_NAME", httpRequest.host());
//        cgiData.put("SERVER_PORT", httpRequest.);
        cgiData.put("CONTENT_TYPE", httpRequest.getHeader(HttpHeaders.CONTENT_TYPE));
        cgiData.put("CONTENT_LENGTH", httpRequest.getHeader(HttpHeaders.CONTENT_LENGTH));
        cgiData.put("REMOTE_ADDR", httpRequest.remoteAddress());
//        cgiData.put("REMOTE_PORT", httpRequest);
        cgiData.put("QUERY_STRING", httpRequest.queryString());
        cgiData.put("PATH_INFO", httpRequest.path());

        return cgiData;
    }

    static String parseCookies(Http.Request httpRequest) {
        Http.Cookies cookies = httpRequest.cookies();

        Iterator<Http.Cookie> itr = cookies.iterator();

        if (cookies == null || !itr.hasNext()) return null;

        StringBuilder builder = new StringBuilder();

        while (itr.hasNext()) {
            String c = itr.next().value();
            if (c == null) continue;

            builder.append(c.trim());

            if (itr.hasNext()) {
                builder.append("; ");
            }
        }

        return builder.toString();
    }
}
