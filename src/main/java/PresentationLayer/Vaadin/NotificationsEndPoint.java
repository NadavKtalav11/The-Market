package PresentationLayer.Vaadin;


import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
//
//@Component
//@ServerEndpoint("/websocket") // WebSocket endpoint path
//public class NotificationsEndPoint {
//
//    private static Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());
//
//
//    @OnOpen
//    public void onOpen(Session session) {
//        sessions.add(session);
//        System.out.println("New client connected: " + session.getId());
//    }
//
//    @OnMessage
//    public void onMessage(String message, Session session) {
//        System.out.println("Message from " + session.getId() + ": " + message);
//    }
//
//    @OnClose
//    public void onClose(Session session) {
//        sessions.remove(session);
//        System.out.println("Client disconnected: " + session.getId());
//    }
//
//    @OnError
//    public void onError(Throwable error) {
//        error.printStackTrace();
//    }
//
//    public static void sendMessageToAll(String message) throws IOException {
//        for (Session session : sessions ) {
//            session.getBasicRemote().sendText(message);
//
//        }
//    }
//
//
//}
