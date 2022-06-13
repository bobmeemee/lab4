package Messages;

public class LeavingNetworkMessage extends Message{
    public LeavingNetworkMessage(String sender) {
        super(sender);
        super.type = "LeavingNetworkMessage";
    }
}
