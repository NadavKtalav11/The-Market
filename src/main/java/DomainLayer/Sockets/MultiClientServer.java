package DomainLayer.Sockets;

import PresentationLayer.Vaadin.webpush.WebPushService;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MultiClientServer {

    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private WebPushService webPushService;

    public MultiClientServer(WebPushService webPushService, int port, int poolSize) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.threadPool = Executors.newFixedThreadPool(poolSize);
        this.webPushService = webPushService;
    }

    public void start() {
        System.out.println("Server started on port " + serverSocket.getLocalPort());
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(new ClientHandler(clientSocket, webPushService));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}