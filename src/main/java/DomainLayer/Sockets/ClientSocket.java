package DomainLayer.Sockets;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.*;

import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Scanner;

public class ClientSocket {

    private final WebSocketClient client = new ReactorNettyWebSocketClient();

    public ClientSocket() {
        // Replace with your WebSocket server URI
        URI serverUri = URI.create("ws://localhost:8080/ws/notifications");

        client.execute(serverUri, session ->
                        Mono.empty()
                                .thenMany(session.receive()
                                        .map(WebSocketMessage::getPayloadAsText)
                                        .doOnNext(System.out::println)
                                        .then())
                                .thenMany(Mono.fromRunnable(() -> {
                                    // Example of sending a message to the server
                                    Scanner scanner = new Scanner(System.in);
                                    while (true) {
                                        System.out.print("Enter message to send: ");
                                        String message = scanner.nextLine();
                                        session.send(Mono.just(session.textMessage(message))).subscribe();
                                    }
                                }))
                                .then())
                .block();
    }

    public static void main(String[] args) {
        new ClientSocket();
    }
}
