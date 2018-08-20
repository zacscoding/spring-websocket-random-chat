package demo.domain;

/**
 * @author zacconding
 * @Date 2018-08-20
 * @GitHub : https://github.com/zacscoding
 */
public class ChatRoom {

    private User user1;
    private User user2;
    private String uuid;

    public ChatRoom(User user1, User user2, String uuid) {
        this.user1 = user1;
        this.user2 = user2;
        this.uuid = uuid;
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "ChatRoom{" + "user1=" + user1 + ", user2=" + user2 + ", uuid='" + uuid + '\'' + '}';
    }
}
