package Messages;

public class DiscoveryMessage extends Message{
    public DiscoveryMessage(String sender) {
        super(sender);
        super.type = "DiscoveryMessage";

    }
}
