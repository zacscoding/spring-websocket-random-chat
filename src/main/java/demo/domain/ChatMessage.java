package demo.domain;

/**
 * @author zacconding
 * @Date 2018-08-20
 * @GitHub : https://github.com/zacscoding
 */
public class ChatMessage {

    private String from;
    private String to;
    private String message;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ChatMessage{" + "from='" + from + '\'' + ", to='" + to + '\'' + ", message='" + message + '\'' + '}';
    }
}
