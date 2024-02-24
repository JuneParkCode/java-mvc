package webserver.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpRequest {
    private HttpMethod method = HttpMethod.GET;
    private String path = "/";
    private HttpHeaders headers = new HttpHeaders();
    private Map<String, String> queries = new HashMap<>();
    private char[] body;

    public HttpRequest(HttpMethod method, String path, HttpHeaders headers, Map<String, String> queries, char[] body) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.queries = queries;
        this.body = body;
    }

    public int getContentLength() {
        Optional<String> value = this.headers.getValue("Content-Length");

        return Integer.parseInt(value.orElse("0"));
    }

    public HttpHeaders getHeaders() {
        return this.headers;
    }

    public String getPath() {
        return this.path;
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public Map<String, String> getQueries() {
        return queries;
    }

    public char[] getBody() {
        return body;
    }

    public void setBody(char[] body) {
        this.body = body;
    }
}
