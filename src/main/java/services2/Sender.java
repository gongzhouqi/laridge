package services2;

import constants.WebConstants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class Sender {
    protected DatagramSocket socket;

    public Sender() {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
            // TODO: consume this
        }
    }

    public void shutDown() {
        socket.close();
    }

    // Should be less than 1000 bytes, 250 chars for safety
    public void send(String info, int toIP, int toPort) {
        String safeInfo = WebConstants.CHECK_CODE + info;
        byte[] arr = safeInfo.getBytes(StandardCharsets.UTF_8);
        try {
            DatagramPacket packet = new DatagramPacket(arr, arr.length, InetAddress.getByName(WebConstants.SUBNET_IP + toIP), toPort);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: consume this
        }
    }
}
