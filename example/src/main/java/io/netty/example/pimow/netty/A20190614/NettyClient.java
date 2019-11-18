package io.netty.example.pimow.netty.A20190614;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author: Pimow
 **/
public class NettyClient {

    public static void main(String[] args) {
        PooledByteBufAllocator aDefault = PooledByteBufAllocator.DEFAULT;
        ByteBuf heapBuffer1 = aDefault.directBuffer(64);
        //1、创建缓冲区
        //2、写入缓冲区内容
        heapBuffer1.writeBytes("client->server".getBytes());

        heapBuffer1.release();

        NioEventLoopGroup work = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        //channel(A.class)这里就是设置一个通道的类型
        bootstrap.group(work).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new FirstClientHandler());
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8000).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功");
            } else {
                System.out.println("连接失败" + future.cause());
            }
        });

        //1、创建缓冲区
        ByteBuf heapBuffer = Unpooled.buffer(64);
        //2、写入缓冲区内容
        heapBuffer.writeBytes("client->server".getBytes());

        heapBuffer.release();
        ChannelFuture channelFuture1 = channelFuture.channel().writeAndFlush(heapBuffer);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        work.shutdownGracefully();

    }
}
