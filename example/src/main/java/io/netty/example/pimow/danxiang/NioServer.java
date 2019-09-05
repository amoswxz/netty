package io.netty.example.pimow.danxiang;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Pimow
 **/
public class NioServer {

    //key=userId value=长连接标识
    private static ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();

    public static void main(String[] args) throws IOException, InterruptedException {
        Selector boss = Selector.open();
        Selector worker = Selector.open();
        new Thread(() -> {
            try {
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
                // 这个操作在jni层设置了fd的状态为非阻塞，然后在调用read的时候可以立马返回
                serverSocketChannel.configureBlocking(false);
                serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 8000));
                serverSocketChannel.register(boss, SelectionKey.OP_ACCEPT);
                System.out.println("1234");
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (true) {
                try {
                    if (boss.select() > 0) {
                        Set<SelectionKey> selectionKeys = boss.selectedKeys();
                        Iterator<SelectionKey> iterator = selectionKeys.iterator();
                        while (iterator.hasNext()) {
                            SelectionKey next = iterator.next();
                            try {
                                if (next.isAcceptable()) {
                                    System.out.println("有链接进来了");
                                    SocketChannel channel = ((ServerSocketChannel) next.channel()).accept();
                                    channel.configureBlocking(false);
                                    channel.register(worker, SelectionKey.OP_READ, System.currentTimeMillis());
                                    System.out.println("有链接进来了");
                                }
                            } finally {
                                System.out.println("执行remove");
                                //这里为啥要删除呢。其实相当于一个水龙头，多个管，一次只能到一个管，如果不删除就一直占用着
                                iterator.remove();
                                next.interestOps(SelectionKey.OP_ACCEPT);
                            }

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            while (true) {
                try {
                    if (worker.select(1) > 0) {
                        Set<SelectionKey> selectionKeys = worker.selectedKeys();
                        Iterator<SelectionKey> iterator = selectionKeys.iterator();
                        while (iterator.hasNext()) {
                            SelectionKey key = iterator.next();
                            if (key.isReadable()) {
                                try {
                                    SocketChannel channel = (SocketChannel) key.channel();
                                    ByteBuffer buffer = ByteBuffer.allocate(4096);
                                    channel.read(buffer);
                                    buffer.flip();
//                                    TransportObject o = JSON
//                                            .parseObject(
//                                                    Charset.defaultCharset().newDecoder().decode(buffer).toString(),
//                                                    new TypeReference<TransportObject>() {
//                                                    });
//                                    concurrentHashMap.put(o.getUserId(), key.attachment());
//                                    System.out.println("用户id=" + o.getUserId() + " 发送消息内容=" + o.getMessage());
                                } finally {
                                    iterator.remove();
                                    //代表重新监听的事件
                                    key.interestOps(SelectionKey.OP_READ);
                                }
                            }

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

