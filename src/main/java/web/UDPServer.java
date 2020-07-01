package web;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {
    public static void main(String[] args) throws IOException {

        DatagramSocket socket = new DatagramSocket();
        byte[] buf = new byte[256];

        while (true) {
            System.out.println(socket.getLocalPort());
            DatagramPacket packet
                    = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            String received
                    = new String(packet.getData(), 0, packet.getLength());

            if (received.equals("end")) {
                break;
            } else {
                System.out.println(received);
            }
        }
        socket.close();
    }
}