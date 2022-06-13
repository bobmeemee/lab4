package Node;

import Messages.LeavingNetworkMessage;
import Messages.Message;
import Utils.HashFunction;
import com.google.gson.Gson;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class RequestHandler extends Thread {
    private Node node;
    private InetAddress multicastAddress;
    private DatagramPacket receivedMessage;

    public RequestHandler(Node node, InetAddress multicastAddress,  DatagramPacket receivedMessage) {
        this.node = node;
        this.multicastAddress = multicastAddress;
        this.receivedMessage = receivedMessage;
    }

    public void run() {
        InetAddress senderIP = receivedMessage.getAddress();
        String json = new String(this.receivedMessage.getData(), 0, this.receivedMessage.getLength());
        Gson gson = new Gson();
        Message message = gson.fromJson(json, Message.class);

        String senderName = message.getSender();
        int senderID = HashFunction.hash(senderName);

        if(senderID == this.node.getNodeID()) {
            return;
        }

        switch (message.getType()) {
            case "DiscoveryMessage":
                // node was the only one in network
                if(this.node.getNodeID() == this.node.getNextID()) {
                    this.node.setNextID(senderID);
                    this.node.setPreviousID(senderID);

                } else if(senderID < this.node.getNextID() && senderID > this.node.getNodeID() ) {
                    this.node.setNextID(senderID);
                    // node is new prev
                } else if(senderID > this.node.getPreviousID() && senderID < this.node.getNodeID() ) {
                    this.node.setPreviousID(senderID);
                }
                break;
            case "LeavingNetworkMessage":
                if(senderID == this.node.getPreviousID())
                    node.setPreviousID(message.getContent());
                if(senderID == this.node.getNextID()) {
                    node.setNextID(message.getContent());
                }
                break;
            case "NodeCountMessage":
                System.out.println("[NODE UDP]: " + "currently " + message.getContent() + " nodes in network");
                if(message.getContent() == 0) {
                    this.node.setNextID(this.node.getNodeID());
                    this.node.setPreviousID(this.node.getNodeID());
                }
                break;

            default:
                break;
        }

    }
}
