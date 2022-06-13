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
        InetAddress senderIP = receivedMessage.getAddress();
        String json = new String(this.receivedMessage.getData(), 0, this.receivedMessage.getLength());
        Gson gson = new Gson();
        Message message = gson.fromJson(json, Message.class);

        String senderName = message.getSender();


        switch(message.getType()) {
            case "DiscoveryMessage":
                try {
                    String s = server.addNode(senderName, senderIP.toString());
                    System.out.println("[NS UDP]: " + s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case "LeavingNetworkMessage":
                try {
                    String s = server.deleteNode(senderName);
                    System.out.println("[NS UDP]: " + s);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            default:
                break;

        }

    }
}
