package io.netty.example.pimow.netty.A20190821.io;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author: Pimow
 **/
public class BioSocketClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("0.0.0.0", 9595);
        socket.getOutputStream().write("我想要回家".getBytes(Charset.defaultCharset()));
    }

}
