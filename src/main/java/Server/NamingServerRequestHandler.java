package Server;


import Messages.Message;
import Utils.HashFunction;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class NamingServerRequestHandler extends Thread {
    private NamingServer server;
    private InetAddress multicastAddress;
    private DatagramPacket receivedMessage;
    private Message response;

    public NamingServerRequestHandler(NamingServer server, InetAddress multicastAddress,  DatagramPacket receivedMessage) {
        this.server = server;
        this.multicastAddress = multicastAddress;
        this.receivedMessage = receivedMessage;
    }

    public void run() {
        String json = new String(this.receivedMessage.getData(), 0, this.receivedMessage.getLength());
        Gson gson = new Gson();
        Message message = gson.fromJson(json, Message.class);

        InetAddress senderIP = receivedMessage.getAddress();
        int senderID = message.getSender();

        if(senderID == this.server.getServerID()) {
            return;
        } else {
            System.out.println("[NS UDP]: received a " + message.getType() + " from " + senderID + " with address "
                    + senderIP + ":" + receivedMessage.getPort());
        }

        Message response = new Message(server.getServerID());
        switch(message.getType()) {
            case "DiscoveryMessage":
                try {
                    String s = server.addNode(senderID, senderIP.toString());
                    System.out.println("[NS UDP]: " + s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case "LeavingNetworkMessage":
                try {
                    String s = server.deleteNode(senderID);
                    System.out.println("[NS UDP]: " + s);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            default:
                break;

        }

    }
}
