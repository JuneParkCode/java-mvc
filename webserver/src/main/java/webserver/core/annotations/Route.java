package webserver.core.annotations;

import webserver.http.HttpMethod;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Route {
    HttpMethod method();
    String path();
}