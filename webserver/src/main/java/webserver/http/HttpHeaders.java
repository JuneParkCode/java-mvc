package webserver.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpHeaders {
    private Map<String, String> headers = new HashMap<>();
    public HttpHeaders() {
        this.headers.put("Content-Type", "text/html;charset=utf-8");
    }

    public Optional<String> getValue(String key) {
        if (this.headers.containsKey(key) == false) {
            return Optional.empty();
        }
        Optional<String> result = Optional.of(this.headers.get(key));
        return result;
    }

    public void setHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();

        headers.forEach((key, value)-> {
            stringBuffer.append(key + ": " + value + "\r\n");
        });
        stringBuffer.append("\r\n");
        return stringBuffer.toString();
    }
}
