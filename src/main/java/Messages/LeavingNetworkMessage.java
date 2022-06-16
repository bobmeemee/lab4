package Messages;

public class LeavingNetworkMessage extends Message {

    public LeavingNetworkMessage(int sender, int replacingNodeID) {
        super(sender);
        super.type = "LeavingNetworkMessage";
        super.content = replacingNodeID;
    }

    @Override
    public int getContent() {
        return this.content;
    }
}
