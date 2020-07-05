package services2;

import constants.WebConstants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

public class Receiver {

    private Thread listener;

    private DatagramSocket socket;

    private AtomicBoolean isRunning;

    private int port;

    public void shutDown() {
        isRunning.set(false);
        socket.close();
        listener.interrupt();
    }

    public Receiver(Core core, int port) {
        this.isRunning = new AtomicBoolean(true);
        try {
            if (port == -1) {
                socket = new DatagramSocket();
                this.port = socket.getLocalPort();
            } else {
                this.port = port;
                socket = new DatagramSocket(port);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        byte[] arr = new byte[1024];
        DatagramPacket packet = new DatagramPacket(arr, arr.length);
        this.listener = new Thread(() -> {
            try {
                while (isRunning.get()) {
                    socket.receive(packet);
                    String info = new String(packet.getData(), 0, packet.getLength());
                    if (info.startsWith(WebConstants.CHECK_CODE)) {
                        InetAddress ip = packet.getAddress();
                        String strIP = ip.getHostAddress();
                        int relativeIP = Integer.parseInt(strIP.substring(strIP.lastIndexOf('.')+1));
                        new Thread(() -> core.process(info.substring(WebConstants.CHECK_CODE.length()), relativeIP)).start();
                    }
                }
                socket.close();
            } catch (IOException ignored) {}
        });
        listener.start();
    }

    public Receiver(Core core) {
        this(core, -1);
    }

    public int getPort() {
        return port;
    }
}
