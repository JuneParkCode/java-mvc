package app.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.core.annotations.Controller;
import webserver.core.annotations.Route;
import webserver.core.exception.BadRequestException;
import webserver.core.exception.NotFoundException;
import app.db.DataBase;
import app.model.User;
import webserver.util.FormData;
import webserver.util.HttpRequestUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;

import static com.google.common.net.HttpHeaders.*;
import static webserver.http.HttpMethod.*;
import static webserver.http.HttpStatus.*;


@Controller(path = "/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    public UserController() {
    }

    @Route(method = GET, path = "/")
    public HttpResponse getStaticFile(HttpRequest request) {
        try {
            URI staticFilePath = new URI("webserver/webapp");
            String projectPath = System.getProperty("user.dir");
            String relativePath = request.getPath();
            Path filePath = Paths.get(projectPath, staticFilePath.getPath(), relativePath);

            return new HttpResponse.HttpResponseBuilder()
                    .setHeader(CONTENT_TYPE, Files.probeContentType(filePath))
                    .setBody(filePath)
                    .build();
        } catch (IOException e) {
            log.info(e.getMessage());
            throw new NotFoundException();
        } catch (URISyntaxException e) { // FIXME: 적절하게 exception 처리하지 않고 있음
            log.info(e.getMessage());
            throw new NotFoundException();
        }
    }

    @Route(method = GET, path = "/create")
    public HttpResponse createUserByGet(HttpRequest request) {
        // TODO: query 받아내는 방식이 비효율적, 개선 필요
        Map<String, String> queries = request.getQueries();
        String userId = queries.get("userId");
        String password = queries.get("password");
        String name = queries.get("name");
        String email = queries.get("email");

        return responseCreateUser(userId, password, name, email);
    }

    private HttpResponse responseCreateUser(String userId, String password, String name, String email) {
        if (userId == null || password == null || name == null || email == null) {
            throw new BadRequestException();
        }

        User user = new User(userId, password, name, email);
        DataBase.addUser(user);

        return new HttpResponse.HttpResponseBuilder()
                .setStatus(FOUND)
                .setHeader("Location", "/index.html")
                .build();
    }


    @Route(method = POST, path = "/create")
    public HttpResponse createUserByPost(HttpRequest request) {
        // TODO: query 받아내는 방식이 비효율적, 개선 필요
        FormData formData = new FormData(request.getBody());

        String userId = formData.getValue("userId");
        String password = formData.getValue("password");
        String name = formData.getValue("name");
        String email = formData.getValue("email");

        return responseCreateUser(userId, password, name, email);
    }

    @Route(method = POST, path = "/login")
    public HttpResponse loginUser(HttpRequest request) {
        FormData formData = new FormData(request.getBody());
        String userId = formData.getValue("userId");
        String password = formData.getValue("password");

        User user = DataBase.findUserById(userId);
        String logined = "true";
        String redirectPath = "/index.html";

        if (user == null || !user.getPassword().equals(password)) {
            logined = "false";
            redirectPath = "/user/login_failed.html";
        }
        return new HttpResponse.HttpResponseBuilder()
                .setStatus(FOUND)
                .setHeader("Location", redirectPath)
                .setCookie("logined", logined)
                .build();
    }

    @Route(method = GET, path = "/list")
    public HttpResponse listUser(HttpRequest request) {
        String cookies = request.getHeaders().getValue("Cookie").orElse("");
        Map<String, String> cookieValues = HttpRequestUtils.parseCookies(cookies);
        String logined = cookieValues.get("logined");

        if (logined.equals("true")) {
            Collection<User> users = DataBase.findAll();
            StringBuffer buffer = new StringBuffer();

            users.forEach((user)->{
                String userString = user.toString();
                buffer.append(userString);
                buffer.append("\n");
            });

            return new HttpResponse.HttpResponseBuilder()
                    .setStatus(OK)
                    .setBody(buffer.toString().getBytes())
                    .build();
        }
        return new HttpResponse.HttpResponseBuilder()
                .setStatus(FOUND)
                .setHeader("Location", "/user/login_failed.html")
                .setCookie("logined", "false")
                .build();
    }
}
