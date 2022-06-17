package Server;

import Messages.FailureMessage;
import Messages.Message;

import java.io.IOException;
import java.net.InetAddress;

public class FailureWatcher extends Thread {

    private final InetAddress address;
    private final int nodeID;
    private final NamingServer server;

    public FailureWatcher(NamingServer server, InetAddress address, int nodeID) {
        this.address = address;
        this.nodeID = nodeID;
        this.server = server;
    }

    @Override
    public void run() {
        System.out.println("[NS UDP]: started FailureWatcher for node " + nodeID);
        while (true) {
            try {
                if(!address.isReachable(5000)) {

                    int belowFailed = server.getLowerNodeID(nodeID);
                    int aboveFailed = server.getUpperNodeID(nodeID);
                    FailureMessage m = new FailureMessage(server.getServerID(), nodeID, belowFailed, aboveFailed);

                    server.getUdpInterface().sendMulticast(m);

                    server.deleteNode(nodeID);
                    System.out.println("[NAMINGSERVER]: Node " + nodeID + " failed");
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}
