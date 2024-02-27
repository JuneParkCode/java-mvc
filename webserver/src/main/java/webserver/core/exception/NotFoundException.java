package webserver.core.exception;

import webserver.http.HttpStatus;

public class NotFoundException extends HttpRequestException{
    public NotFoundException() {
        super(HttpStatus.NOT_FOUND, "Not Found");
    }

    public NotFoundException(String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }
}
