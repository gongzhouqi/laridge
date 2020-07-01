package web;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class UDPClient {


    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        System.out.println(socket.getLocalPort());
        String str = "end";
        byte[] arr = str.getBytes(StandardCharsets.UTF_8);
        //四个参数: 包的数据  包的长度  主机对象  端口号
        DatagramPacket packet = new DatagramPacket
                (arr, arr.length,InetAddress.getByName("10.0.0.14") , 61382);
        socket.send(packet);

        System.out.println(socket.getPort());
        socket.close();
    }

}