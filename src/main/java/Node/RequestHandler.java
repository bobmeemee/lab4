package Node;

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

    }
}
