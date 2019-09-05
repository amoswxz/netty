package io.netty.example.pimow.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author: Pimow
 **/
public class BioServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1919);
        new Thread(() -> {
            while (true) {
                try {
                    Socket accept = serverSocket.accept();
                    new Thread(() -> {
                        try {
                            InputStream inputStream = accept.getInputStream();
                            int len;
                            byte[] bytes = new byte[1024];
                            while ((len = inputStream.read(bytes)) != -1) {
                                System.out.println(new String(bytes, 0, len));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
