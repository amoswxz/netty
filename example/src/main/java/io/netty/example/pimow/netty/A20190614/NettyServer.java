package io.netty.example.pimow.netty.A20190614;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.HashedWheelTimer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author: Pimow
 **/
public class NettyServer {


    private static final ThreadLocal<WeakHashMap> a = new ThreadLocal();

    public static void main(String[] args) throws InterruptedException {
    ThreadLocal<String> b = new ThreadLocal();
        b.set("111");
        b.remove();


        WeakHashMap<User, String> weakHashMap = new WeakHashMap();
        User zhangsan = new User("zhangsan", 24);
        weakHashMap.put(zhangsan, "zhangsan");
        //强引用
        System.out.println("有强引用的时候:map大小" + weakHashMap.size());
        //去掉强引用
        zhangsan = null;
        System.gc();
        Thread.sleep(1000);
        System.out.println("无强引用的时候:map大小" + weakHashMap.size());

        int i = normalizeTicksPerWheel(10);
        System.out.println(i);
        int i1 = normalizeTicksPerWheel(3);
        System.out.println(i1);
        int i2 = normalizeTicksPerWheel(8);
        System.out.println(i2);

        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup work = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, work).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new FirstServerHandler());
//                        ch.pipeline().addLast(new SecondServerHandler());
                    }
                });
        serverBootstrap.bind(8000);
    }


    private static int normalizeTicksPerWheel(int ticksPerWheel) {
        int normalizedTicksPerWheel = 1;
        while (normalizedTicksPerWheel < ticksPerWheel) {
            normalizedTicksPerWheel <<= 1;
        }
        return normalizedTicksPerWheel;
    }
}
