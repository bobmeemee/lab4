package Messages;

public class NodeCountMessage extends Message {

    private final int nodeCount;

    public NodeCountMessage(int sender, int nodeCount) {
        super(sender);
        super.type = "NodeCountMessage";
        this.nodeCount = nodeCount;
    }

    @Override
    public int getContent() {
        return this.nodeCount;
    }
}
