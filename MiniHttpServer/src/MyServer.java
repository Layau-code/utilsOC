import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Handler;

public class MyServer {
    public static void main(String[] args) throws IOException {
        //监听8080端口
        HttpServer server = HttpServer.create(new InetSocketAddress(8080),0);

        //创建一个HttpHandler
        HttpHandler handler = new MiniHandler();

        //如果有请求，就交给handler
        server.createContext("/helloHttp",handler);
        //启动服务器
        server.start();
        System.out.println("服务器启动成功");
    }
}
