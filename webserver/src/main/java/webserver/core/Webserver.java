package webserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Webserver {
    private static final Logger log = LoggerFactory.getLogger(Webserver.class);
   private int port = 0;

    public Webserver(int portNumber) {
        port = portNumber;
    }

    public void start() {
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("Webserver started port {}", port);

            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                RequestHandler handler = new RequestHandler(connection);
                handler.start();
            }

        } catch (IOException e) {
            log.error("Listen socket creation failed");
        }
    }
}
