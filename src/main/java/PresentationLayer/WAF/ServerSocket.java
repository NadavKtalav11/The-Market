package PresentationLayer.WAF;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.DataListener;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;

public class ServerSocket {
    private SocketIOServer server;

    public ServerSocket() {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(80);

        this.server = new SocketIOServer(config);

        this.server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(com.corundumstudio.socketio.SocketIOClient client) {
                String token = getUserToken(client);
                if (token != null && !token.isEmpty()) {
                    String username = getUserName(token);
                    if (username != null && !username.isEmpty()) {
                        joinRoom(username, client);
                    } else {
                        client.disconnect();
                    }
                } else {
                    client.disconnect();
                }
            }
        });

        this.server.addEventListener("notification", String.class, new DataListener<String>() {
            @Override
            public void onData(com.corundumstudio.socketio.SocketIOClient client, String message, AckRequest ackRequest) throws Exception {
                String username = getUsernameFromClient(client);
                if (username != null && !username.isEmpty()) {
                    sendNotificationToUser(username, message);
                }
            }
        });
    }

    private String getUserToken(com.corundumstudio.socketio.SocketIOClient client) {
        // Implement function to get user token
        return null; // Placeholder implementation
    }

    private String getUserName(String token) {
        // Implement function to get user name based on token
        return null; // Placeholder implementation
    }

    private void joinRoom(String username, com.corundumstudio.socketio.SocketIOClient client) {
        client.joinRoom(username);
    }

    private String getUsernameFromClient(com.corundumstudio.socketio.SocketIOClient client) {
        // Implement function to get username from client
        return null; // Placeholder implementation
    }

    private void sendNotificationToUser(String username, String message) {
        server.getRoomOperations(username).sendEvent("notification", message);
    }

    public void start() {
        this.server.start();
        System.out.println("Socket.IO server started on port " + this.server.getConfiguration().getPort());
    }

    public void stop() {
        this.server.stop();
        System.out.println("Socket.IO server stopped");
    }
}
