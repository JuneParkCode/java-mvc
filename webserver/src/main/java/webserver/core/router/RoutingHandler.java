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
            // NOTE: redirect case 에 대해서 따로 처리가 필요함.
            response.send(dos);
            log.info("Response {} {}", response.getStatus(), request.getPath());
        } catch (HttpRequestException e) {
            // filter exceptions.
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
        } catch (RuntimeException e) {
            e.printStackTrace();

            HttpResponse response = buildFailedResponse(INTERNAL_SERVER_ERROR);
            response.send(dos);
            log.info("Response {} {}", INTERNAL_SERVER_ERROR, request.getPath());
        }
    }

    default public HttpResponse buildFailedResponse(HttpStatus statusCode) {
        // NOTE: 유연하게 변경할 수 있도록 할 수 있으면 좋음.
        String body = "<html><body><h1>" + statusCode.getReason() + "</h1></body></html>";;

        return new HttpResponse.HttpResponseBuilder()
                .setStatus(statusCode)
                .setBody(body.getBytes())
                .build();
    }
}
