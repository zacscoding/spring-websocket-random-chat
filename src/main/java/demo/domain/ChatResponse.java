package demo.domain;

/**
 * @author zacconding
 * @Date 2018-08-22
 * @GitHub : https://github.com/zacscoding
 */
public class ChatResponse {

    private String chatRoomId;
    private String sessionId;

    public ChatResponse() {
    }

    public ChatResponse(String chatRoomId, String sessionId) {
        this.chatRoomId = chatRoomId;
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    @Override
    public String toString() {
        return "ChatResponse{" + "sessionId='" + sessionId + '\'' + ", chatRoomId='" + chatRoomId + '\'' + '}';
    }
}
