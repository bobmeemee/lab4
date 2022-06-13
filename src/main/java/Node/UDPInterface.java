package Node;

import java.io.IOException;
import java.net.*;

public class UDPInterface implements Runnable {
    private final Node node;
    private final InetAddress multicastAddress = InetAddress.getByName("255.255.255.255");

    public UDPInterface(Node node) throws UnknownHostException {
        this.node = node;
    }




    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket();

            byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            Thread rq = new Thread( new RequestHandler(node, multicastAddress,packet));
            rq.start();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
