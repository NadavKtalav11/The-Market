package DomainLayer.Notifications;

public interface Observer {
    boolean update(Notification newNotification);
}
