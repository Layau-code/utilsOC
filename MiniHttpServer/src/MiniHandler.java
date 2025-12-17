import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.format.ResolverStyle;

public class MiniHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
         //1.获取URL的参数
        String query= exchange.getRequestURI().getQuery();
        //拿到第一个参数
        String name=query.split("name=")[1];
        //2.以json格式返回
        String response = "{ \"code\": 200, \"message\": \"OK\", \"data\": \"Hello, " + name + "!\" }";

        //3.发送回复
        exchange.getResponseHeaders().set("Content-Type","application/json;charset=utf-8");;
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os=exchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
}
