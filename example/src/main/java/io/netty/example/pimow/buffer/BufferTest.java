package io.netty.example.pimow.buffer;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

/**
 * @author: Pimow
 **/
public class BufferTest {

    public static void main(String[] args) throws CharacterCodingException, UnsupportedEncodingException {
        System.out.println(Charset.defaultCharset());
        String str = "阿里巴巴";
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        System.out.println("=============初始  " + buffer.position());
        System.out.println("=============初始  " + buffer.limit());
        System.out.println("=============初始  " + buffer.capacity());

        buffer.put(str.getBytes());
        System.out.println("=============put   " + buffer.position());
        System.out.println("=============put   " + buffer.limit());
        System.out.println("=============put   " + buffer.capacity());

        //写模式切换为读模式。position=0；limit=之前的position。capacity不变
        buffer.flip();
        System.out.println("=============flip  " + buffer.position());
        System.out.println("=============flip  " + buffer.limit());
        System.out.println("=============flip  " + buffer.capacity());

        //把position设置为0，limit=capacity
//        buffer.clear();
//        System.out.println("=============flip  " + buffer.position());
//        System.out.println("=============flip  " + buffer.limit());
//        System.out.println("=============flip  " + buffer.capacity());

        //get
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes, 0, 3);
        System.out.println("=============get  " + buffer.position());
        System.out.println("=============get  " + buffer.limit());
        System.out.println("=============get  " + buffer.capacity());

//        buffer.rewind();
        buffer.compact();
        int remaining = buffer.remaining();
        System.out.println(remaining);
        System.out.println("=============rewind  " + buffer.position());
        System.out.println("=============rewind  " + buffer.limit());
        System.out.println("=============rewind  " + buffer.capacity());
    }

}
