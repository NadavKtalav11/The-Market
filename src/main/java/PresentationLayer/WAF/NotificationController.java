//package PresentationLayer.WAF;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//
//@Controller
//public class NotificationController {
//
//    private final SimpMessagingTemplate messagingTemplate;
//
//    @Autowired
//    public NotificationController(SimpMessagingTemplate messagingTemplate) {
//        this.messagingTemplate = messagingTemplate;
//    }
//
//    public void sendNotification(String message) {
//        messagingTemplate.convertAndSend("/topic/notifications", message);
//    }
//}
