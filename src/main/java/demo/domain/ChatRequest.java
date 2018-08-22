package demo.domain;

import java.util.Objects;

/**
 * @author zacconding
 * @Date 2018-08-22
 * @GitHub : https://github.com/zacscoding
 */
public class ChatRequest {

    private String sessionId;

    public ChatRequest() {
    }

    public ChatRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChatRequest)) {
            return false;
        }

        ChatRequest that = (ChatRequest) o;
        return Objects.equals(this.sessionId, that.sessionId);
    }

    @Override
    public int hashCode() {
        return sessionId.hashCode();
    }

    @Override
    public String toString() {
        return "ChatRequest{" + "sessionId='" + sessionId + '\'' + '}';
    }
}