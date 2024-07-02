package PresentationLayer.Vaadin;

import DomainLayer.Notifications.NotificationFacade;
//import DomainLayer.Notifications.Publisher;
import jdk.jfr.Event;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {
 /// todo add map
    NotificationFacade notificationFacade;
    private static final Map<String, WebSocketSession> sessionsByUser = new ConcurrentHashMap<>();
    private static MyWebSocketHandler instance;

    private MyWebSocketHandler(){
    }
    public static synchronized MyWebSocketHandler getInstance(){
        if(instance == null){
            instance = new MyWebSocketHandler();
        }
        return instance;
    }



    public void handleStringMessage(String userId, String message) throws Exception {
        // Handle incoming messages
        WebSocketSession session = sessionsByUser.get(userId);
        if (session==null){
            notificationFacade.sendLateMessage(userId, message);
        }
        session.sendMessage(new TextMessage(message));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        // add session to map
        String sessionId = (String) session.getAttributes().get("userID");
        if (sessionId != null) {
            sessionsByUser.put(sessionId, session);
        }
//        Event event = new Event(new Object(), "connect message from server!!!",new HashSet<>(Arrays.asList(u)));
//        Publisher publisher = (Publisher) SpringContext.getBean("Publisher");
//        publisher.publish(event);


    }

    // todo after connection closed()


    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userName = (String) session.getAttributes().get("username");
        if (userName != null) {
            sessionsByUser.remove(userName);
        }
    }

}