package webserver.core.router;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.*;
import webserver.core.exception.HttpRequestException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.function.Function;

import static webserver.http.HttpStatus.*;

public interface RoutingHandler extends Function<HttpRequest, HttpResponse> {
    static final Logger log = LoggerFactory.getLogger(RoutingHandler.class);
    default public void response(DataOutputStream dos, HttpRequest request) throws IOException {
        try {
            HttpResponse response = this.apply(request);
            response.send(dos);
            // NOTE: 해당 부분 refactor 여지 있음. send() 항상 log 가 남겨져야함.
            log.info("Response {} {}", response.getStatus(), request.getPath());
        } catch (HttpRequestException e) {
            filterHttpException(dos, request, e);
        } catch (RuntimeException e) {
            buildFailedResponse(INTERNAL_SERVER_ERROR).send(dos);
            log.info("Response {} {}", INTERNAL_SERVER_ERROR, request.getPath());
        }
    }

    private void filterHttpException(DataOutputStream dos, HttpRequest request, HttpRequestException e) throws IOException {
        HttpStatus statusCode = e.getStatusCode();
        HttpResponse response;

        switch (statusCode) {
            case BAD_REQUEST:
            case UNAUTHORIZED:
            case NOT_FOUND:
            case CONFLICT:
                response = buildFailedResponse(statusCode);
                break;
            default:
                response = buildFailedResponse(BAD_REQUEST);
                break;
        }
        response.send(dos);
        log.info("Response {} {}", statusCode, request.getPath());
    }

    default public HttpResponse buildFailedResponse(HttpStatus statusCode) {
        // NOTE: File 형태로 response 하는게 조금 더 바람직해보임.
        String body = "<html><body><h1>" + statusCode.getReason() + "</h1></body></html>";;

        return new HttpResponse.HttpResponseBuilder()
                .setStatus(statusCode)
                .setBody(body.getBytes())
                .build();
    }
}
