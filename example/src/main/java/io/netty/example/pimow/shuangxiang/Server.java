package io.netty.example.pimow.shuangxiang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Server {

    private final static int port = 6001;
    //解码buffer
    // private CharsetDecoder decode = Charset.forName("UTF-8").newDecoder();
    /*发送数据缓冲区*/
    private ByteBuffer sBuffer = ByteBuffer.allocate(1024);
    /*接受数据缓冲区*/
    private ByteBuffer rBuffer = ByteBuffer.allocate(1024);
    /*映射客户端channel */
    private String sendText;
    private Map<String, SocketChannel> clientsMap = new HashMap<>();
    private Charset charset = Charset.forName("UTF-8");
    private int i = 0;

    private Server() {
        try {
            listen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Selector selector;
    private Selector workSelector;

    /**
     * 服务器端轮询监听，select方法会一直阻塞直到有相关事件发生或超时
     */
    private void listen() throws IOException {
        selector = Selector.open();
        workSelector = Selector.open();

        new Thread(() -> {
            /*
             *启动服务器端，配置为非阻塞，绑定端口，注册accept事件
             *ACCEPT事件：当服务端收到客户端连接请求时，触发该事件
             */
            ServerSocketChannel serverSocketChannel;
            try {
                serverSocketChannel = ServerSocketChannel.open();
                serverSocketChannel.configureBlocking(false);
                serverSocketChannel.bind(new InetSocketAddress(port));
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            } catch (Exception e) {
                e.printStackTrace();
            }

            while (true) {
                try {
                    if (selector.select() > 0) {
                        //返回值为本次触发的事件数
                        Set<SelectionKey> selectionKeys = selector.selectedKeys();
                        for (SelectionKey key : selectionKeys) {
                            handle(key);
                        }
                        selectionKeys.clear();//清除处理过的事件
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }

            }
        }).start();

        new Thread(() -> {
            while (true) {
                try {
                    if (workSelector.select(1) > 0) {//返回值为本次触发的事件数
                        Set<SelectionKey> selectionKeys = workSelector.selectedKeys();
                        for (SelectionKey key : selectionKeys) {
                            workHandle(key);
                        }
                        selectionKeys.clear();//清除处理过的事件
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }).start();


    }

    /**
     * 处理不同的事件
     */
    private void handle(SelectionKey selectionKey) throws IOException {
        if (selectionKey.isAcceptable()) {
            /*
             * 客户端请求连接事件
             * serversocket为该客户端建立socket连接，将此socket注册READ事件，监听客户端输入
             * READ事件：当客户端发来数据，并已被服务器控制线程正确读取时，触发该事件
             */
            ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
            SocketChannel client = server.accept();
            clientsMap.put(client.getLocalAddress().toString().substring(1) + i++, client);
            client.configureBlocking(false);
            client.register(workSelector, SelectionKey.OP_READ);
            System.out.println("有链接加入");
            new Thread(() -> {
                while (true) {
                    try {
                        InputStreamReader input = new InputStreamReader(System.in);
                        BufferedReader br = new BufferedReader(input);
                        sendText = br.readLine();
                        if (!clientsMap.isEmpty()) {
                            for (Entry<String, SocketChannel> entry : clientsMap.entrySet()) {
                                SocketChannel temp = entry.getValue();
                                sBuffer.clear();
                                sBuffer.put(("server send message" + sendText).getBytes("UTF-8"));
                                sBuffer.flip();
                                //输出到通道
                                temp.write(sBuffer);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }).start();
        }

    }


    private void workHandle(SelectionKey selectionKey) throws IOException {
        String receiveText;
        int count;
        if (selectionKey.isReadable()) {
            /*
             * READ事件，收到客户端发送数据，读取数据后继续注册监听客户端
             */
            SocketChannel client = (SocketChannel) selectionKey.channel();
            rBuffer.clear();
            count = client.read(rBuffer);
            if (count > 0) {
                rBuffer.flip();
                receiveText = charset.decode(rBuffer.asReadOnlyBuffer()).toString();
                System.out.println(receiveText);
                new Thread(() -> {
                    while (true) {
                        try {
                            InputStreamReader input = new InputStreamReader(System.in);
                            BufferedReader br = new BufferedReader(input);
                            sendText = br.readLine();
                            if (!clientsMap.isEmpty()) {
                                for (Entry<String, SocketChannel> entry : clientsMap.entrySet()) {
                                    SocketChannel temp = entry.getValue();
                                    String name = entry.getKey();
                                    sBuffer.clear();
                                    sBuffer.put(("server send message : 读取" + sendText).getBytes("UTF-8"));
                                    sBuffer.flip();
                                    //输出到通道
                                    temp.write(sBuffer);
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }).start();
                selectionKey.interestOps(SelectionKey.OP_READ);
            }
        }

    }

    public static void main(String[] args) {
        new Server();
    }
}

