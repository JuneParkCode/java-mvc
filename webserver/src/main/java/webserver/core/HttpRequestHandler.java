package webserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpParser;
import webserver.http.HttpRequest;
import webserver.core.router.Router;
import webserver.core.router.RoutingHandler;

import java.io.*;
import java.net.Socket;

public class HttpRequestHandler extends RequestHandler {
    private static final Logger log = LoggerFactory.getLogger(HttpRequestHandler.class);
    private final Socket conn;
    public HttpRequestHandler(Socket connection) {
        conn = connection;
    }
    @Override
    public void handle() {
        log.info("new client connected. Connected IP : {}, Port : {}", conn.getInetAddress(), conn.getPort());

        Router router = Router.getInstance(); // singleton
        try (InputStream in = conn.getInputStream(); OutputStream out = conn.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            HttpParser parser = new HttpParser();
            // get request
            HttpRequest request = parser.parse(br);
            // get route handler
            RoutingHandler routingHandler = router.route(request);
            // response to client
            routingHandler.response(dos, request);
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (HttpParser.MalformedHttpRequestException e) {
            log.error("Request is malformed IP {} PORT {}", conn.getInetAddress(), conn.getPort());
        }
    }

}
