package io.netty.example.pimow.bio;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

/**
 * @author: Pimow
 **/
public class BioClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("127.0.0.1", 1919);
        while (true) {
            socket.getOutputStream().write((new Date() + "hello word").getBytes());
            Thread.sleep(2000);
        }
    }
}
