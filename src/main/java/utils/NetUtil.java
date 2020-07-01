package utils;

import constants.WebConstants;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class NetUtil {

    private static class IPWrapper {
        InetAddress localIP;

        private void setLocalIP(InetAddress localIP) {
            this.localIP = localIP;
        }
    }

    public static int getLocalAddress() {
        IPWrapper ip = new IPWrapper();

        String macAddress = "MAC" + new Random().nextDouble();
        try {
            InetAddress dummy = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(dummy);
            byte[] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (byte b : mac) {
                sb.append(String.format("%02X", b));
            }
            macAddress = sb.toString();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }

        String finalMacAddress = macAddress;
        Thread t = new Thread(() -> {
            try {
                DatagramSocket serverSocket = new DatagramSocket(WebConstants.INIT_PORT);
                byte[] arr = new byte[1024];
                DatagramPacket packet = new DatagramPacket(arr, arr.length);
                while (true) {
                    serverSocket.receive(packet);
                    String info = new String(packet.getData(), 0, packet.getLength());
                    if (info.equals(finalMacAddress)) {
                        ip.setLocalIP(packet.getAddress());
                        break;
                    }
                }
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();

        byte[] arr = macAddress.getBytes(StandardCharsets.UTF_8);
        try {
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(arr, arr.length, InetAddress.getByName(WebConstants.SUBNET_IP + WebConstants.BROADCAST_SUFFIX), WebConstants.INIT_PORT);
            socket.send(packet);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            t.join();
        } catch (InterruptedException ignored) {}
        String strIP = ip.localIP.getHostAddress();
        return Integer.parseInt(strIP.substring(strIP.lastIndexOf('.')+1));
    }
}
