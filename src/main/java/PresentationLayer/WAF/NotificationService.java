package PresentationLayer.WAF;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNotification(String message) {
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }

    // Example of scheduled notifications
    @Scheduled(fixedRate = 5000)
    public void sendPeriodicNotifications() {
        sendNotification("Periodic Notification: " + System.currentTimeMillis());
    }

    public void sendNotificationToUser(String userId, String message) {
        messagingTemplate.convertAndSendToUser(userId, "/topic/notifications", message);
    }
}

