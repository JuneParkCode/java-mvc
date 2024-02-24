package webserver.core.router;

import webserver.http.HttpMethod;
import webserver.http.HttpRequest;

import java.util.Objects;

public class RoutingPath implements Comparable<RoutingPath> {
    private HttpMethod method;
    private String path;
    public RoutingPath(HttpRequest request) {
        this.method = request.getMethod();
        this.path = request.getPath();
    }

    public RoutingPath(HttpMethod method, String path) {
        this.method = method;
        this.path = path;
    }

    public String serialize() {
        return method.toString() + " " + path;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RoutingPath that = (RoutingPath) obj;
        return method == that.method && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, path);
    }
    @Override
    public int compareTo(RoutingPath o) {
        return this.serialize().compareTo(o.serialize());
    }
}
