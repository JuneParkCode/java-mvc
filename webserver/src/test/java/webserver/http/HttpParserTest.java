package webserver.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class HttpParserTest {
    private HttpParser parser;

    @BeforeEach
    public void setup() {
        parser = new HttpParser();
    }

    @Test
    public void verify_start_line_with_absolute_path() {
        // given
        String httpRequest
                = "GET http://test.com/user/1 HTTP/1.1\r\n";
        // when
        HttpRequest request = parser.parse(new BufferedReader(new StringReader(httpRequest)));

        // then
        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals("/user/1", request.getPath());
    }


    @Test
    public void verify_start_line_with_relative_path() {
        // given
        String httpRequest
                = "GET /user/1 HTTP/1.1\r\n";
        // when
        HttpRequest request = parser.parse(new BufferedReader(new StringReader(httpRequest)));

        // then
        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals("/user/1", request.getPath());
    }

    @Test
    public void verify_start_line_with_query() {
        // given
        String httpRequest
                = "GET /user/1?name=test HTTP/1.1\r\n";
        // when
        HttpRequest request = parser.parse(new BufferedReader(new StringReader(httpRequest)));

        // then
        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals("/user/1", request.getPath());
        assertEquals("test", request.getQueries().get("name"));
    }

    @Test
    public void verify_body() {
        // given
        String httpRequest = "POST /user/login HTTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n" +
                "Content-Length: 25\r\n" +
                "\r\n" +
                "userId=test&password=test";
        // when
        HttpRequest request = parser.parse(new BufferedReader(new StringReader(httpRequest)));

        // then
        assertEquals("userId=test&password=test", new String(request.getBody()));
    }

    @Test
    public void verify_parse_headers() {
        String httpRequest
                = "GET http://test.com/user/1?name=test HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n" +
                "Accept-Language: en-US,en;q=0.5\r\n" +
                "Accept-Encoding: gzip, deflate\r\n" +
                "Connection: keep-alive\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "If-Modified-Since: Sat,  Jul 1997 05:00:00 GMT\r\n" +
                "Cache-Control: no-cache\r\n";

        HttpRequest request = parser.parse(new BufferedReader(new StringReader(httpRequest)));

        assertEquals("localhost", request.getHeaders().getValue("Host").get());
        assertEquals("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3", request.getHeaders().getValue("User-Agent").get());
        assertEquals("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8", request.getHeaders().getValue("Accept").get());
        assertEquals("en-US,en;q=0.5", request.getHeaders().getValue("Accept-Language").get());
        assertEquals("gzip, deflate", request.getHeaders().getValue("Accept-Encoding").get());
        assertEquals("keep-alive", request.getHeaders().getValue("Connection").get());
        assertEquals("1", request.getHeaders().getValue("Upgrade-Insecure-Requests").get());
        assertEquals("no-cache", request.getHeaders().getValue("Cache-Control").get());
    }
}