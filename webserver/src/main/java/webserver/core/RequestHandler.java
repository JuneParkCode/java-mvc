package webserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket conn;
    public RequestHandler(Socket connection) {
        conn = connection;
    }
    @Override
    public void run() {
        log.info("new client connected. Connected IP : {}, Port : {}", conn.getInetAddress(), conn.getPort());

        /**
         * @TODO 현재 해당 코드는 Request 와 관련 없이 단순 "Hello World" 만을 Client 에게 전송하고 있음.
         * 해당 코드는 다음과 같이 진행되어야함.
         * 1. Get request from socket
         * 2. Get response from request
         * 3. Sen response to client
         */
        try (InputStream in = conn.getInputStream(); OutputStream out = conn.getOutputStream()) {
            byte[] body = "Hello World".getBytes();
            DataOutputStream dos = new DataOutputStream(out);

            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
