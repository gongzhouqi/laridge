package web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;
import java.net.ServerSocket;

public class Receive {
    public static void main(String[] args) {
        File target;    //接收到的文件保存的位置
        FileOutputStream save;  //将接收到的文件写入电脑
        FileInputStream in;     //读取穿送过来的数据文件
        ServerSocket server;    //服务器
        Socket socket;          //套接字

        try {
            String s = "fileToReceive/f1.txt";
            target = new File(s);
            save = new FileOutputStream(target);

            server = new ServerSocket(2017);    //服务端口

            //等待客户端的呼叫
            System.out.println("正在等待客户端的呼叫........");
            socket = server.accept();   //阻塞
            in = (FileInputStream)socket.getInputStream();

            //接收数据
            byte[] b = new byte[64];
            int n = in.read(b);
            while (n != -1) {
                save.write(b, 0, n);    //写入指定地方
                n = in.read(b);
            }
            socket.close();
            server.close();
            in.close();
            save.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}