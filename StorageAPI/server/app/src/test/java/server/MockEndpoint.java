package server.endpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MockEndpoint implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException{
        System.out.println("ping received");
        t.getRequestBody();
        t.sendResponseHeaders(200, -1);

    }
}
