package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.google.common.net.HttpHeaders.*;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    // NOTE: Version 에 대해서 별도로 처리가 필요함
    private final String version = "HTTP/1.1";
    private HttpStatus status;
    private HttpHeaders headers;
    private byte[] body = {};

    public HttpResponse() {
        status = HttpStatus.OK;
        headers = new HttpHeaders();
    }

    public HttpResponse(HttpStatus status, HttpHeaders header) {
        this.status = status;
        this.headers = header;
        this.body = new byte[0];
    }

    public HttpResponse(HttpStatus status, HttpHeaders header, byte[] body) {
        this.status = status;
        this.headers = header;
        this.body = body;
    }

    public static class HttpResponseBuilder {
        private final String version = "HTTP/1.1";
        private HttpStatus status = HttpStatus.OK;
        private HttpHeaders headers = new HttpHeaders();
        private byte[] body = {};


        public HttpResponseBuilder() {
        }

        public HttpResponse build() {
            return new HttpResponse(this.status, this.headers, this.body);
        }

        public HttpResponseBuilder setStatus(HttpStatus status) {
            this.status = status;
            return this;
        }

        public HttpResponseBuilder setHeader(String key, String value) {
            this.headers.setHeader(key, value);
            return this;
        }

        // NOTE: Path 지정에 대해서 생각해볼 필요 있음.
        public HttpResponseBuilder setCookie(String key, String value) {
            String cookie = this.headers.getValue("Cookie").orElse("Path=/;");

            cookie = key + "=" + value + ";" + cookie;
            this.headers.setHeader("Set-Cookie", cookie);
            return this;
        }

        public HttpResponseBuilder setBody(Path filePath) throws IOException {
            this.body = Files.readAllBytes(filePath);
            return this;
        }
        public HttpResponseBuilder setBody(byte[] body) {
            this.body = body;
            return this;
        }
    }

    public void send(DataOutputStream dos) throws IOException {
        String startLine = version + " " + this.status.getValue() + " " + this.status.getReason() + "\r\n";

        dos.writeBytes(startLine);
        dos.writeBytes(this.headers.toString());
        dos.write(this.body, 0, this.body.length);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public byte[] getBody() {
        return body;
    }


    public void setBody(Path filePath) throws IOException {
        setBody(Files.readAllBytes(filePath));
    }
    public void setBody(byte[] body) {
        this.body = body;
        this.headers.setHeader(CONTENT_LENGTH, Integer.toString(body.length));
    }
}
