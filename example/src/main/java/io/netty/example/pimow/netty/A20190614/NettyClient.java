package io.netty.example.pimow.netty.A20190614;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.nio.channels.Selector;
import sun.nio.ch.SelectorImpl;

/**
 * @author: Pimow
 **/
public class NettyClient {

    public static void main(String[] args) {
        NioEventLoopGroup work = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        //channel(A.class)这里就是设置一个通道的类型
        bootstrap.group(work).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer() {
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(new FirstClientHandler());
            }
        });
        bootstrap.connect("127.0.0.1", 8000).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功");
            } else {
                System.out.println("连接失败");
            }
        });
//        work.shutdownGracefully();
    }
}
