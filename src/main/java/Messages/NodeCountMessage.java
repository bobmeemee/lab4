package Messages;

public class NodeCountMessage extends Message {
    public NodeCountMessage(String sender) {
        super(sender);
        super.type = "NodeCountMessage";

    }
}
