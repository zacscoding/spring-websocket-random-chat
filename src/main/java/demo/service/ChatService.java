package demo.service;

import demo.domain.ChatRoom;
import demo.domain.User;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author zacconding
 * @Date 2018-08-20
 * @GitHub : https://github.com/zacscoding
 */
@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    private Map<User, DeferredResult<ChatRoom>> waitingUsers;
    private ReentrantReadWriteLock lock;

    @PostConstruct
    private void setUp() {
        this.waitingUsers = new LinkedHashMap<>();
        this.lock = new ReentrantReadWriteLock();
    }

    @Async("asyncThreadPool")
    public void joinChatRoom(User user, DeferredResult<ChatRoom> deferredResult) {
        System.out.println("## joinChatRoom :: " + Thread.currentThread().getName() + "-" + Thread.currentThread().getId());
        if (user == null || deferredResult == null) {
            return;
        }

        try {
            lock.writeLock().lock();
            waitingUsers.put(user, deferredResult);
        } finally {
            lock.writeLock().unlock();
            establishChatRoom();
        }
    }

    public void cancelChatRoom(User user) {
        try {
            lock.writeLock().lock();
        } finally {
            lock.writeLock().unlock();
            establishChatRoom();
        }
    }

    public void establishChatRoom() {
        try {
            lock.readLock().lock();
            if (waitingUsers.size() < 2) {
                return;
            }

            Iterator<User> itr = waitingUsers.keySet().iterator();
            User user1 = itr.next();
            User user2 = itr.next();

            String uuid = UUID.randomUUID().toString();
            DeferredResult<ChatRoom> user1Result = waitingUsers.remove(user1);
            DeferredResult<ChatRoom> user2Result = waitingUsers.remove(user2);

            ChatRoom chatRoom = new ChatRoom(user1, user2, uuid);
            user1Result.setResult(chatRoom);
            user2Result.setResult(chatRoom);

            logger.info(">> establish chat room.\nChatRoom : {}", chatRoom);
        } catch (Exception e) {
            logger.warn("Exception occur while checking waiting users", e);
        } finally {
            lock.readLock().unlock();
        }
    }
}