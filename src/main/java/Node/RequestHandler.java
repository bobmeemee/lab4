package Node;

import Messages.*;
import Utils.HashFunction;
import com.google.gson.Gson;

import java.io.IOException;
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
        int senderID = message.getSender();

        if(senderID == this.node.getNodeID()) {
            return;
        } else {
            System.out.println("[NODE UDP]: received a " + message.getType() + " from " + senderID + " with address "
                    + senderIP + ":" + receivedMessage.getPort());
        }

        Message response = new Message(senderID);
        boolean sendUnicastResponse = false;

        switch (message.getType()) {
            case "DiscoveryMessage":
                // node was the only one in network
                if(this.node.getNodeID() == this.node.getNextID() && this.node.getNextID() != -1) {
                    this.node.setNextID(senderID);
                    this.node.setPreviousID(senderID);
                    response = new InsertAsPreviousAndNextMessage(this.node.getNodeID());
                    sendUnicastResponse = true;
                    System.out.println("[NODE]: new next " + this.node.getNextID());
                    System.out.println("[NODE]: new previous " + this.node.getPreviousID());
                    // sender is new next
                } else if((senderID < this.node.getNextID()) && (senderID > this.node.getNodeID())
                        || (this.node.getNextID() == this.node.getPreviousID() && senderID>this.node.getNodeID()) ) {
                    this.node.setNextID(senderID);
                    response = new InsertAsPreviousMessage(this.node.getNodeID());
                    sendUnicastResponse = true;
                    System.out.println("[NODE]: new next " + this.node.getNextID());
                    System.out.println("[NODE]: previous " + this.node.getPreviousID());
                    // sender is new prev
                } else if(senderID > this.node.getPreviousID() && senderID < this.node.getNodeID()
                        || (this.node.getNextID() == this.node.getPreviousID() && senderID < this.node.getNodeID())) {
                    this.node.setPreviousID(senderID);
                    response = new InsertAsNextMessage(this.node.getNodeID());
                    sendUnicastResponse = true;
                    System.out.println("[NODE]: next " + this.node.getNextID());
                    System.out.println("[NODE]: new previous " + this.node.getPreviousID());

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
                System.out.println("[NODE UDP]: " + "currently " + message.getContent() + " other nodes in network");
                if(message.getContent() == 0) {
                    this.node.setNextID(this.node.getNodeID());
                    this.node.setPreviousID(this.node.getNodeID());
                    System.out.println("[NODE]: Only node in network");
                    System.out.println("[NODE]: nextNodeID: " + this.node.getNextID());
                    System.out.println("[NODE]: previousNodeID: " + this.node.getPreviousID());
                }
                break;

            case "InsertAsNextMessage":
                this.node.setNextID(senderID);
                System.out.println("[NODE UDP]: New next node ID: " + this.node.getNextID());
                break;
            case "InsertAsPreviousMessage" :
                this.node.setPreviousID(senderID);
                System.out.println("[NODE UDP]: New previous node ID: " + this.node.getPreviousID());
                break;

            case "InsertAsPreviousAndNextMessage":
                this.node.setPreviousID(senderID);
                this.node.setNextID(senderID);
                System.out.println("[NODE UDP]: New previous and next node ID: " + this.node.getPreviousID());

            default:
                break;
        }

        if(sendUnicastResponse) {
            try {
                this.node.getUdpInterface().sendUnicast(response, senderIP, receivedMessage.getPort());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
