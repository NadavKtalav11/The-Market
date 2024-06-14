package DomainLayer.Sockets;

import PresentationLayer.Vaadin.webpush.WebPushService;

import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private WebPushService webPushService;

    public ClientHandler(Socket socket, WebPushService webPushService) {
        this.clientSocket = socket;
        this.webPushService = webPushService;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                processCommand(inputLine);
                out.println("Command received: " + inputLine);
                if ("bye".equalsIgnoreCase(inputLine)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processCommand(String command) {
        String[] parts = command.split(" ", 3);
        if (parts.length < 3) {
            return;  // Ignore invalid commands
        }

        String action = parts[0];
        String userId = parts[1];
        String message = parts[2];

        if ("notifyUser".equals(action)) {
            webPushService.notifyUser(userId, message);
        } else {
            System.out.println("Unknown command: " + command);
        }
    }
}