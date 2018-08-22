package demo.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

/**
 * @author zacconding
 * @Date 2018-08-22
 * @GitHub : https://github.com/zacscoding
 */
@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("------------ Connect ----------");
        // headerAccessor.getSessionId();
        String roomId = headerAccessor.getNativeHeader("roomId").get(0);
        String userId = headerAccessor.getNativeHeader("userId").get(0);
        logger.info("## Check room id : {} | user id : {}");

        System.out.println(headerAccessor);
        System.out.println("--------------------------------");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        System.out.println("------------ Disconnect ----------");
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        // headerAccessor.getSessionId();
        System.out.println(headerAccessor);
        System.out.println("--------------------------------");
    }
}
