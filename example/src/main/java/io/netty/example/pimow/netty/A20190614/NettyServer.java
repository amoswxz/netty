package io.netty.example.pimow.netty.A20190614;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.WeakHashMap;

/**
 * @author: Pimow
 **/
public class NettyServer {


    private static final ThreadLocal<WeakHashMap> a = new ThreadLocal();

    public static void main(String[] args) throws InterruptedException {
        Object o = AccessController.doPrivileged(new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                return "abc";
            }
        });

        System.out.println(o);


//        ThreadLocal<String> b = new ThreadLocal();
//        b.set("111");
//        b.remove();
//
//
//        WeakHashMap<User, String> weakHashMap = new WeakHashMap();
//        User zhangsan = new User("zhangsan", 24);
//        weakHashMap.put(zhangsan, "zhangsan");
//        //强引用
//        System.out.println("有强引用的时候:map大小" + weakHashMap.size());
//        //去掉强引用
//        zhangsan = null;
//        System.gc();
//        Thread.sleep(1000);
//        System.out.println("无强引用的时候:map大小" + weakHashMap.size());
//
//        int i = normalizeTicksPerWheel(10);
//        System.out.println(i);
//        int i1 = normalizeTicksPerWheel(3);
//        System.out.println(i1);
//        int i2 = normalizeTicksPerWheel(8);
//        System.out.println(i2);

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, work).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        System.out.println(11111);
                        ch.pipeline().addLast(new FirstServerHandler());
                    }
                });
        serverBootstrap.bind(8000).sync();
    }


    private static int normalizeTicksPerWheel(int ticksPerWheel) {
        int normalizedTicksPerWheel = 1;
        while (normalizedTicksPerWheel < ticksPerWheel) {
            normalizedTicksPerWheel <<= 1;
        }
        return normalizedTicksPerWheel;
    }
}
