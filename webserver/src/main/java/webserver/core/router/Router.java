package webserver.core.router;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.*;
import webserver.core.annotations.Controller;
import webserver.core.annotations.Route;
import webserver.core.exception.HttpRequestException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Router {
    // routing 관련한 부분은 이후 개선해야함
    private static final Logger log = LoggerFactory.getLogger(Router.class);
    private final Map<RoutingPath, RoutingHandler> routingTable;
    private static Router instance = null;

    private Router() {
        routingTable = new HashMap<>();
    }

    public static Router getInstance() {
        if (instance == null) {
            instance = new Router();
        }
        return instance;
    }

    public void registerNewMethod(RoutingPath path, RoutingHandler method) {
        routingTable.put(path, method);
    }

    public void registerController(Object controller) {
        if (!controller.getClass().isAnnotationPresent(Controller.class)) {
            return;
        }
        Controller controllerAnnotation = controller.getClass().getAnnotation(Controller.class);
        String rootPath = controllerAnnotation.path();
        registerRoutingHandlers(controller, rootPath);
    }

    private void registerRoutingHandlers(Object controller, String rootPath) {
        for (Method method : controller.getClass().getMethods()) {
            if (method.isAnnotationPresent(Route.class)) {
                Route route = method.getAnnotation(Route.class);
                HttpMethod httpMethod = route.method();
                String path = getPath(rootPath, route);

                RoutingHandler routingHandler = getRoutingHandler(controller, method);
                this.registerNewMethod(new RoutingPath(httpMethod, path), routingHandler);
            }
        }
    }

    @NotNull
    private static RoutingHandler getRoutingHandler(Object controller, Method method) {
        return (request) -> {
            try {
                return (HttpResponse) method.invoke(controller, request);
            } catch (InvocationTargetException e) {
                Throwable exception = e.getTargetException();
                if (exception instanceof HttpRequestException) {
                    throw (HttpRequestException) exception;
                }
                throw new RuntimeException(exception);
            } catch (IllegalAccessException e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }
        };
    }

    @NotNull
    private static String getPath(String rootPath, Route route) {
        String routerPath = route.path();
        if (routerPath.equals("/")) {
            routerPath = "";
        }
        String path = rootPath + routerPath;
        return path;
    }

    @NotNull
    public RoutingHandler route(HttpRequest request) {
        String path = request.getPath();
        HttpMethod method = request.getMethod();
        RoutingPath key = new RoutingPath(method, path);

        while (path.length() > 0) {
            if (routingTable.containsKey(key)) {
                return routingTable.get(key);
            }
            path = getParentPath(path);
            key = new RoutingPath(method, path);
        }
        return getNotFoundRoutingHandler();
    }

    // NOTE: 해당 class 의 책임이 아님
    @NotNull
    private String getParentPath(String path) {
        int lastSlashIndex = path.lastIndexOf('/');

        if (path.equals("/")) {
            return "";
        }
        if (lastSlashIndex == 0) {
            return "/";
        }
        return path.substring(0, lastSlashIndex);
    }

    // NOTE: 별도의 file 로 빼는 것이 조금 더 적합함.
    @NotNull
    private RoutingHandler getNotFoundRoutingHandler() {
        byte[] body = "<html><body>NOT FOUND</body></html>".getBytes();

        return (request) -> new HttpResponse.HttpResponseBuilder().setBody(body).build();
    }

}
