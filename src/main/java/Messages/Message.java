package Messages;

public class Message {
    protected String type;
    protected String sender;

    public Message(String sender) {
        this.sender = sender;

    }

    public String getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }
}
