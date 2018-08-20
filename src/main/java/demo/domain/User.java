package demo.domain;

import java.util.Objects;

/**
 * @author zacconding
 * @Date 2018-08-20
 * @GitHub : https://github.com/zacscoding
 */
public class User {

    private String name;
    private String sessionId;

    public User() {
    }

    public User(String name, String sessionId) {
        this.name = name;
        this.sessionId = sessionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

        if (!(o instanceof User)) {
            return false;
        }

        User user = (User) o;
        return Objects.equals(this.sessionId, user.sessionId);
    }

    @Override
    public int hashCode() {
        return sessionId.hashCode();
    }

    @Override
    public String toString() {
        return "User{" + "name='" + name + '\'' + ", sessionId='" + sessionId + '\'' + '}';
    }
}