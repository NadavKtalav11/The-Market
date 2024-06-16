package PresentationLayer.WAF;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;

@Controller
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @MessageMapping("/sendMessage")
    @SendToUser("/topic/notifications") // use @SendToUser instead of @SendTo
    public String sendMessage(String storeId, Principal principal) throws Exception {
        notificationService.addUserName(principal.getName()); // store UUID
        Thread.sleep(1000); // simulated delay
        return "";
    }
}