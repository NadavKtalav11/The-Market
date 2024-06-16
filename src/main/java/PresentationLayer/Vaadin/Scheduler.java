package PresentationLayer.Vaadin;

import PresentationLayer.WAF.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;



@Component
public class Scheduler {
    private final NotificationService notificationService;

    Scheduler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Scheduled(fixedRateString = "6000", initialDelayString = "0")
    public void schedulingTask() {
        notificationService.sendMessages();
    }
}

