package webserver.core.router;

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

    // TODO: 해당 코드 이해 안되어있음.
    public void registerController(Object controller) {
        if (!controller.getClass().isAnnotationPresent(Controller.class)) {
            return;
        }
        Controller controllerAnnotation = controller.getClass().getAnnotation(Controller.class);
        String rootPath = controllerAnnotation.path();

        for (Method method : controller.getClass().getMethods()) {
            if (method.isAnnotationPresent(Route.class)) {
                Route route = method.getAnnotation(Route.class);
                HttpMethod httpMethod = route.method();
                // NOTE: refactoring 필요
                String routerPath = route.path();
                if (routerPath.equals("/")) {
                    routerPath = "";
                }
                String path = rootPath + routerPath;
                RoutingHandler routingHandler = (request) -> {
                    try {
                        return (HttpResponse) method.invoke(controller, request);
                    } catch (InvocationTargetException e) {
                        // NOTE: invoke 된 method 의 exception 은 InvocationTargetException 으로 Wrapping 됨
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
                this.registerNewMethod(new RoutingPath(httpMethod, path), routingHandler);
            }
        }
    }

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
        return getNotFoundController();
    }

    // NOTE: 해당 class 의 책임이 아님
    private String getParentPath(String path) {
        int lastSlashIndex = path.lastIndexOf('/');
        if (lastSlashIndex == 0)
            return "/";
        if (lastSlashIndex == -1) {
            path = "";
        } else {
            path = path.substring(0, lastSlashIndex);
        }
        return path;
    }

    // NOTE: 별도의 file 로 빼는 것이 조금 더 적합함.
    private RoutingHandler getNotFoundController() {
        byte[] body = "<html><body>NOT FOUND</body></html>".getBytes();

        return (request) -> new HttpResponse.HttpResponseBuilder().setBody(body).build();
    }

}
