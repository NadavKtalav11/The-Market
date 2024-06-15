package PresentationLayer.Vaadin;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.socket.WebSocketHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

public class CustomHandShakeHandler extends DefaultHandshakeHandler {

//    public CustomHandShakeHandler (TomcatRequestUpgradeStrategy tomcatRequestUpgradeStrategy){
//        super(tomcatRequestUpgradeStrategy);
//    }

    @Override
    public Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        // generate user name by UUID
        return new StompPrincipal(UUID.randomUUID().toString());
    }
}

