package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class HttpParser {
    private static final Logger log = LoggerFactory.getLogger(HttpParser.class);
    private HttpMethod method;
    private HttpHeaders headers = new HttpHeaders();
    private String path;
    private Map<String, String> queries;

    private char[] body = null;

    public HttpRequest parse(BufferedReader br) throws MalformedHttpRequestException {
        try {
            String headerSection = parseRequestHeader(br);
            String[] lines = headerSection.split("\r\n");

            parseStartLine(lines);
            parseHeaders(lines);
            parseBody(br);
            return new HttpRequest(method, path, headers, queries, body);
        } catch (Exception e) {
            log.debug(e.getMessage());
            throw new MalformedHttpRequestException();
        }
    }

    private void parseBody(BufferedReader br) throws IOException {
        int contentLength = Integer.parseInt(headers.getValue("Content-Length").orElse("0"));
        if (contentLength > 0) {
            this.body = new char[contentLength];
            br.read(this.body, 0, contentLength);
        }
    }

    private String parseRequestHeader(BufferedReader bufferedReader) throws IOException {
        StringBuffer buffer = new StringBuffer();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            if (line.length() == 0)
                break;
            buffer.append(line);
            buffer.append("\r\n");
        }
        return buffer.toString();
    }

    private void parseStartLine(String[] lines) throws URISyntaxException {
        String startLine = lines[0];

        String[] words = startLine.split(" ");
        String methodString = words[0];
        URI uri = new URI(words[1]);

        this.method = HttpMethod.valueOf(methodString);
        this.path = uri.getPath();
        this.queries = getQueryFromString(uri.getQuery());
    }

    private void parseHeaders(String[] lines) {
        // start line is 0
        for (int i = 1; i < lines.length; ++i) {
            String[] kv = lines[i].split(":");
            String key = kv[0].trim();
            String value = kv[1].trim();

            headers.setHeader(key, value);
        }
    }

    private Map<String, String> getQueryFromString(String queryString) {
        HashMap<String, String> queries = new HashMap<>();
        if (queryString == null)
            return queries;
        String[] splitString = queryString.split("&");

        for (String query : splitString) {
            String[] kv = query.split("=");
            String key = kv[0];
            String value = kv[1];

            queries.put(key, value);
        }
        return queries;
    }

    public class MalformedHttpRequestException extends RuntimeException {
    }
}
