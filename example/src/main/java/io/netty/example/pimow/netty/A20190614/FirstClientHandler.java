package io.netty.example.pimow.netty.A20190614;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * @author: Pimow
 **/
public class FirstClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("客户端写数据");
        ByteBuf buffer = ctx.alloc().buffer();
        System.out.println(buffer.capacity());
        System.out.println(buffer.maxCapacity());
        byte[] bytes = ("皮摩~我").getBytes(Charset.forName("utf-8"));
        buffer.writeBytes(bytes);
        buffer.retain();
        ctx.channel().writeAndFlush(buffer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(new Date() + ": 客户端读到数据 -> " + byteBuf.toString(Charset.forName("utf-8")));
    }

    public static void main(String[] args) {
        System.out.println(11 % 3);
        byte[] bytes = ("皮摩我").getBytes(Charset.forName("utf-8"));
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeBytes(bytes);
        System.out.println(buffer.readerIndex());
        System.out.println(buffer.writerIndex());
        System.out.println(buffer.readableBytes());
        System.out.println(buffer.maxCapacity());
        System.out.println("-----------------");
        ByteBuf duplicate = buffer.duplicate();
        System.out.println(buffer.readerIndex());
        System.out.println(buffer.writerIndex());
        System.out.println(duplicate.capacity());
        System.out.println(duplicate.maxCapacity());
        System.out.println(duplicate.writeBytes("我".getBytes()));
        System.out.println("-----------------");
        ByteBuf slice = buffer.slice();
        System.out.println(slice.capacity());
        System.out.println(slice.maxCapacity());
        System.out.println("-----------------");
        System.out.println(buffer.readerIndex());
        System.out.println(buffer.writerIndex());
        System.out.println(buffer.readableBytes());
        System.out.println(buffer.maxCapacity());
    }
}
