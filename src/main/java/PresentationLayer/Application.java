package PresentationLayer;

import PresentationLayer.WAF.ServerSocket;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
//import io.socket.engineio.server.EngineIoServer;
//import io.socket.engineio.server.EngineIoServerOptions;
//import io.socket.engineio.server.transport.WebSocket;
//import io.socket.engineio.server.transport.WebSocketTransport;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */


import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"PresentationLayer.WAF", "PresentationLayer.Vaadin.webpush"})
@Theme(value = "webpush")
@PWA(name = "Web Push", shortName = "Push")
@EnableScheduling
public class Application implements AppShellConfigurator {
    private ServerSocket serverSocket;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
