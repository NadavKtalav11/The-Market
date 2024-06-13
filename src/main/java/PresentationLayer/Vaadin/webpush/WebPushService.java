package PresentationLayer.Vaadin.webpush;

import DomainLayer.Sockets.MultiClientServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class WebPushService {

    @Value("${vapid.public.key}")
    private String publicKey;
    @Value("${vapid.private.key}")
    private String privateKey;
    @Value("${vapid.subject}")
    private String subject;

    private PushService pushService;
    private final Map<String, Subscription> endpointToSubscription = new HashMap<>();
    private final WebSocketHandler webSocketHandler;

    @Autowired
    public WebPushService(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @PostConstruct
    private void init() throws GeneralSecurityException {
        Security.addProvider(new BouncyCastleProvider());
        pushService = new PushService(publicKey, privateKey, subject);
        int port = 8080;  // Your chosen port
        int poolSize = 1000;  // Your chosen thread pool size
        try {
            MultiClientServer server = new MultiClientServer(this, port, poolSize);
            new Thread(server::start).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void sendNotification(Subscription subscription, String messageJson) {
        try {
            HttpResponse response = pushService.send(new Notification(subscription, messageJson));
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 201) {
                System.out.println("Server error, status code:" + statusCode);
                InputStream content = response.getEntity().getContent();
                List<String> strings = IOUtils.readLines(content, "UTF-8");
                System.out.println(strings);
            }
        } catch (GeneralSecurityException | IOException | JoseException | ExecutionException
                | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(Subscription subscription) {
        System.out.println("Subscribed to " + subscription.endpoint);
        endpointToSubscription.put(subscription.endpoint, subscription);
    }

    public void unsubscribe(Subscription subscription) {
        System.out.println("Unsubscribed " + subscription.endpoint + " auth:" + subscription.keys.auth);
        endpointToSubscription.remove(subscription.endpoint);
    }

    public void notifyAll(String title, String body) {
        try {
            String msg = mapper.writeValueAsString(new Message(title, body));
            endpointToSubscription.values().forEach(subscription -> sendNotification(subscription, msg));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void notifyUser(String userId, String message) {
        webSocketHandler.sendMessageToUser(userId, message);
    }

    public record Message(String title, String body) {
    }

    private final ObjectMapper mapper = new ObjectMapper();
}
