package io.netty.example.pimow.netty.A20190614;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * @author: Pimow
 **/
public class FirstServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(new Date() + ": 服务端读到数据 -> " + byteBuf.toString(Charset.defaultCharset()));
        ctx.channel().writeAndFlush(ctx.alloc().buffer().writeBytes("ojbk".getBytes()));
        ctx.writeAndFlush(ctx.alloc().buffer().writeBytes("ojbk".getBytes()));
    }
}
