package web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;
import java.net.InetAddress;

public class Send {
    public static void main(String[] args) {
        File filesrc;
        Socket socket;
        FileInputStream open;
        FileOutputStream out;

        try {
            String src = "fileToSend/f1.txt";

            filesrc = new File(src);
            open = new FileInputStream(filesrc);

            String url = InetAddress.getLocalHost().getHostAddress();;

            socket = new Socket(url, 2017);//创建socket

            out = (FileOutputStream)socket.getOutputStream();//创建文件输出流

            //开始传送
            byte[] b = new byte[1024];
            int n = open.read(b);

            while (n != -1) {
                out.write(b, 0, n);
                n = open.read(b);
            }
            out.close();
            socket.close();
            open.close();
        } catch (Exception e) {
            System.out.println("文件路径或者ip有误");
        }
    }
}