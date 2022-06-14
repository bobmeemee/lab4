package Server;

import java.io.IOException;
import java.net.*;

public class NamingServerUDPInterface implements Runnable{
    private final NamingServer server;
    private final InetAddress multicastAddress = InetAddress.getByName("255.255.255.255");

    public NamingServerUDPInterface(NamingServer server) throws UnknownHostException {
        this.server = server;
    }


    @Override
    public void run() {
        try {
            while (true) {
                DatagramSocket socket = new DatagramSocket(8000);
                byte[] buf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                if(packet.getLength() == 0) {
                    System.out.println("[NS UDP]: message is empty");
                } else {
                    Thread rq = new Thread(new NamingServerRequestHandler(server, multicastAddress, packet));
                    rq.start();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
