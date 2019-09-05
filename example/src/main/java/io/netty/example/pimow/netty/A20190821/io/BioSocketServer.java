package io.netty.example.pimow.netty.A20190821.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author: Pimow
 **/
public class BioSocketServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9595);
        Socket accept = serverSocket.accept();
        int len=0;
        byte[] bytes = new byte[1024];
        InputStream inputStream = accept.getInputStream();
        while ((len+=inputStream.read(bytes)) != -1) {
            System.out.println(new String(bytes,0,len));
        }
    }
}
