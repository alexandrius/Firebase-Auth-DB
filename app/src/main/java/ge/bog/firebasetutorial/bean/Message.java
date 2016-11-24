package ge.bog.firebasetutorial.bean;

/**
 * Created by alex on 11/24/2016
 */

public class Message {
    private String messageText;
    private String messageUser;
    private long messageTime;

    public Message() {
        messageTime = System.currentTimeMillis();
    }

    public Message(String messageText, String messageUser) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        messageTime = System.currentTimeMillis();
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }
}
