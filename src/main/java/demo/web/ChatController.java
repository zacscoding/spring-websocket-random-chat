package demo.web;

import demo.domain.ChatRoom;
import demo.domain.User;
import demo.service.ChatService;
import demo.util.ServletUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author zacconding
 * @Date 2018-08-20
 * @GitHub : https://github.com/zacscoding
 */
@RestController
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ChatService chatService;

    // tag :: async
    @GetMapping("/join/{userName}")
    @ResponseBody
    public DeferredResult<ChatRoom> joinRequest(@PathVariable("userName") String userName) {
        String sessionId = ServletUtil.getSession().getId();
        logger.info(">> Join request. userName : {} , session id : {}", userName, sessionId);

        final User user = new User(userName, sessionId);
        final DeferredResult<ChatRoom> deferredResult = new DeferredResult<>(null);
        chatService.joinChatRoom(user, deferredResult);

        deferredResult.onCompletion(() -> chatService.cancelChatRoom(user));
        deferredResult.onError((throwable) -> chatService.cancelChatRoom(user));
        deferredResult.onTimeout(() -> chatService.cancelChatRoom(user));

        return deferredResult;
    }

    // -- tag :: async

    // tag :: websocket stomp
    @MessageMapping("/chat.message/{uuid}")
    public void sendMessage(@DestinationVariable("uuid") String uuid) {

    }

    // -- tag :: websocket stomp
}
