package PresentationLayer.WAF;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;

public class ClientSocket {
    private SocketIOServer server;

    public ClientSocket() {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(8080); // Set the port of your server

        this.server = new SocketIOServer(config);
    }

    public void connect() {
        this.server.connect(new SocketIONamespace() {
            @Override
            public void onStart(SocketIOServer server) {
                System.out.println("Connected to server");
            }

            @Override
            public void onStop(SocketIOServer server) {
                System.out.println("Disconnected from server");
            }

            @Override
            public void onConnect(SocketIOClient client) {
                System.out.println("Connected to server: " + client.getSessionId());
            }

            @Override
            public void onDisconnect(SocketIOClient client) {
                System.out.println("Disconnected from server: " + client.getSessionId());
            }

            @Override
            public void onConnectError(SocketIOClient client, Throwable throwable) {
                System.out.println("Connection error for client " + client.getSessionId() + ": " + throwable.getMessage());
            }

            @Override
            public void onError(SocketIOClient client, Throwable throwable) {
                System.out.println("Error for client " + client.getSessionId() + ": " + throwable.getMessage());
            }
        });
    }

    public void disconnect() {
        this.server.stop();
    }

    public static void main(String[] args) {
        ClientSocket clientSocket = new ClientSocket();
        clientSocket.connect();

        // Add your client logic here

        clientSocket.disconnect();
    }
}

