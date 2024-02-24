package app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.core.Webserver;

public class WebserverApplication {
    private static final Logger log = LoggerFactory.getLogger(WebserverApplication.class);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args != null && args.length > 0)
            port = Integer.parseInt(args[0]);

        Webserver server = new Webserver(port);
        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}