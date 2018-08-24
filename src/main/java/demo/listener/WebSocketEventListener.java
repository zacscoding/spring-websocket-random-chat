package demo.listener;

import demo.service.ChatService;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.messaging.support.NativeMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * @author zacconding
 * @Date 2018-08-22
 * @GitHub : https://github.com/zacscoding
 */
@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private ChatService chatService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        MessageHeaderAccessor accessor = NativeMessageHeaderAccessor.getAccessor(event.getMessage(), SimpMessageHeaderAccessor.class);
        GenericMessage<?> generic = (GenericMessage<?>) accessor.getHeader("simpConnectMessage");
        Map<String, Object> nativeHeaders = (Map<String, Object>) generic.getHeaders().get("nativeHeaders");
        String chatRoomId = ((List<String>) nativeHeaders.get("chatRoomId")).get(0);
        String sessionId = (String) generic.getHeaders().get("simpSessionId");

        /*StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("## headerAccessor :: " + headerAccessor);
        String chatRoomId = headerAccessor.getNativeHeader("chatRoomId").get(0);
        String sessionId = headerAccessor.getSessionId();*/

        logger.info("[Connected] room id : {} | websocket session id : {}", chatRoomId, sessionId);

        chatService.connectUser(chatRoomId, sessionId);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String sessionId = headerAccessor.getSessionId();

        logger.info("[Disconnected] websocket session id : {}", sessionId);

        chatService.disconnectUser(sessionId);
    }
}
