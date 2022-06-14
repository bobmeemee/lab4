package Messages;

public class NodeCountMessage extends Message {
    public NodeCountMessage(int sender) {
        super(sender);
        super.type = "NodeCountMessage";

    }
}
