package io.netty.example.pimow.danxiang;

import io.netty.example.pimow.danxiang.pojo.TransportObject;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * @author: Pimow
 **/
public class NioClient {

    private int a=1;

    public static void main(String[] args) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.connect(new InetSocketAddress("127.0.0.1", 8000));
        //建立缓冲区
        ByteBuffer writeBuf = ByteBuffer.allocate(1024);
        while (true) {
            Scanner scan = new Scanner(System.in);
            String str = scan.nextLine();
            TransportObject transportObject = new TransportObject();
            transportObject.setUserId(123);
            transportObject.setMessage(str);
//            writeBuf.put(JSON.toJSONBytes(transportObject));
            writeBuf.flip();
            channel.write(writeBuf);
            writeBuf.clear();
        }

    }


}
