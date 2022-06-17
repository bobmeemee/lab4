package Server;

import Messages.FailureMessage;
import Messages.Message;

import java.io.IOException;
import java.net.InetAddress;

public class FailureWatcher implements Runnable {

    private final InetAddress address;
    private final int nodeID;
    private final NamingServer server;
    volatile boolean shutdown = false;

    public FailureWatcher(NamingServer server, InetAddress address, int nodeID) {
        this.address = address;
        this.nodeID = nodeID;
        this.server = server;
    }

    public void setShutdown(boolean shutdown) {
        this.shutdown = shutdown;
    }

    @Override
    public void run() {
        while (!shutdown) {
            try {
                if(!address.isReachable(5000)) {

                    int belowFailed = server.getLowerNodeID(nodeID);
                    int aboveFailed = server.getUpperNodeID(nodeID);
                    FailureMessage m = new FailureMessage(server.getServerID(), nodeID, belowFailed, aboveFailed);
                    if( !shutdown) {
                        server.getUdpInterface().sendMulticast(m);
                    }
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
