package app.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpMethod;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.core.annotations.Controller;
import webserver.core.annotations.Route;
import webserver.core.exception.NotFoundException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.google.common.net.HttpHeaders.*;

@Controller(path = "/")
public class MainController {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @Route(method = HttpMethod.GET, path = "/")
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

}
