package io.netty.example.pimow.netty.A20190614;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Random;

/**
 * @author: Pimow
 **/
public class FirstServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(new Date() + ": 服务端读到数据 -> " + byteBuf.toString(Charset.forName("utf-8")));
        byte[] bytes = "谢谢你关注皮摩~".getBytes(Charset.forName("utf-8"));
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes(bytes);
        ctx.channel().writeAndFlush(buffer);

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().write("测试顺序");
    }

    private static Random rng = new Random(2019);

    private static double avgRtt = 50.28901734104046;

    public static void main(String[] args) {
        long sum = 0;
        for (int i = 0; i < 12; i++) {
            long rtt = nextRTT();
            sum += rtt;
            System.out.println("rtt=" + rtt);
        }
        System.out.println("sum=" + sum);
    }

    private static long nextRTT() {
        double u = rng.nextDouble();
        int x = 0;
        double cdf = 0;
        while (u >= cdf) {
            x++;
            cdf = 1 - Math.exp(-1.0D * 1 / avgRtt * x);
        }
        return x;
    }
}
