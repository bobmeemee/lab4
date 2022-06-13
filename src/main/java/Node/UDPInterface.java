package Node;

import Messages.Message;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.*;

public class UDPInterface implements Runnable {
    private final Node node;
    private final InetAddress multicastAddress = InetAddress.getByName("255.255.255.255");
    private DatagramSocket socket;

    public UDPInterface(Node node) throws UnknownHostException, SocketException {
        this.node = node;
        DatagramSocket socket = new DatagramSocket(node.getPort());
    }

    public void sendMulticast(Message m) throws IOException {
        String json = new Gson().toJson(m);

        byte[] buf = json.getBytes();

        DatagramPacket packet = new DatagramPacket(buf, buf.length, this.multicastAddress, this.node.getPort());
        this.socket.send(packet);
        System.out.println("[NODE UDP]: Discovery message sent");

    }


    @Override
    public void run() {
        try {
            byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            this.socket.receive(packet);
            System.out.println("[NS UDP]: received a message");
            Thread rq = new Thread( new RequestHandler(node, multicastAddress,packet));
            rq.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
