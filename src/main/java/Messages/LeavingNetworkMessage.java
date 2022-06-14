package Messages;

public class LeavingNetworkMessage extends Message {
    private int replacingNodeID;

    public LeavingNetworkMessage(int sender, int replacingNodeID) {
        super(sender);
        super.type = "LeavingNetworkMessage";
        this.replacingNodeID = replacingNodeID;
    }

    @Override
    public int getContent() {
        return replacingNodeID;
    }
}
