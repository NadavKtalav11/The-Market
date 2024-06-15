package DomainLayer.Sockets;


import java.io.IOException;

public class SocketFacade {
    private MultiClientServer serverSocket;
    private static SocketFacade socketFacadeInstance;


    public SocketFacade() throws IOException {
        //serverSocket = new MultiClientServer();
    }

    public static synchronized SocketFacade getInstance() throws IOException {
        if (socketFacadeInstance == null) {
            socketFacadeInstance = new SocketFacade();
        }
        return socketFacadeInstance;
    }
}