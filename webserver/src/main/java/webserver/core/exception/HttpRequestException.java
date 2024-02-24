package webserver.core.exception;

import webserver.http.HttpStatus;

public class HttpRequestException extends RuntimeException {
    private HttpStatus statusCode;

    public HttpRequestException(HttpStatus statusCode, String reason) {
        super(reason);
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
