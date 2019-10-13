package io.netty.example.pimow.netty.A20190614;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.nio.charset.StandardCharsets;

/**
 * @author: Pimow
 **/
public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("--------------" + byteBuf.toString(StandardCharsets.UTF_8));
        ByteBuf buffer = ctx.alloc().buffer();
        byte[] bytes = ("服务端发送的数据").getBytes(StandardCharsets.UTF_8);
        buffer.writeBytes(bytes);
        ctx.channel().writeAndFlush(buffer);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server second channel active");
    }
}
