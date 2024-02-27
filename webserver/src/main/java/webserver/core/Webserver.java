package webserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import app.controllers.MainController;
import app.controllers.UserController;
import webserver.core.router.Router;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

public class Webserver {
    private static final Logger log = LoggerFactory.getLogger(Webserver.class);
   private int port = 0;

    public Webserver(int portNumber) {
        port = portNumber;
    }

    public void start() throws InvocationTargetException, IllegalAccessException {
        // initialize router
        // NOTE: 이상적인 형태는 아니다. 이후에 변경해야함.
        Router router = Router.getInstance();
        router.registerController(new MainController());
        router.registerController(new UserController());

        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("Webserver started port {}", port);

            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                RequestHandler handler = new HttpRequestHandler(connection);
                handler.start();
            }

        } catch (IOException e) {
            log.error("Listen socket creation failed");
        }
    }
}
