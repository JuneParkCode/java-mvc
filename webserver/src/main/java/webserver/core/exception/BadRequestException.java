package webserver.core.exception;

import webserver.http.HttpStatus;

public class BadRequestException extends HttpRequestException{
    public BadRequestException() {
        super(HttpStatus.BAD_REQUEST, "Bad Request");
    }

    public BadRequestException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}
