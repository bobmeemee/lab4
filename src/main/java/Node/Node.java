package Node;


import Messages.DiscoveryMessage;
import Messages.LeavingNetworkMessage;
import Messages.Message;
import Utils.HashFunction;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Node {
    private String name;
    private int nodeID;
    private int nextID;
    private int previousID;
    private UDPInterface udpInterface;

    public Node(String name) throws IOException {
        this.name = name;
        this.nodeID = HashFunction.hash(name);
        nextID = -1;
        previousID = -1;

        try {
            this.udpInterface = new UDPInterface(this);
            new Thread(this.udpInterface).start();
        } catch (Exception e) {
            System.err.println("[NS] " + e);
        }


        this.discovery();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("[NODE] Shutdown hook");
                try {
                    shutdown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public int getNodeID() {
        return nodeID;
    }

    public UDPInterface getUdpInterface() {
        return udpInterface;
    }

    public int getNextID() {return nextID;}
    public void setNextID(int nextID) {this.nextID = nextID;}
    public int getPreviousID() {return previousID;}
    public void setPreviousID(int previousID) {this.previousID = previousID;}

    public void discovery() throws IOException {
        Message m = new DiscoveryMessage(this.nodeID);
        udpInterface.sendMulticast(m);
    }

    public void shutdown() throws IOException {
        Message m1 = new LeavingNetworkMessage(this.nodeID, this.nextID);
        Message m2 = new LeavingNetworkMessage(this.nodeID, this.previousID);

        udpInterface.sendMulticast(m1);
        udpInterface.sendMulticast(m2);
    }


    public static void main(String[] args) throws IOException {
        Node node = new Node(args[0]);
    }

}
