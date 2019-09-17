package io.netty.example.pimow.netty;

import io.netty.channel.ChannelHandler.Sharable;

/**
 * @author: Pimow
 **/
public class TestSharable {

    public static void main(String[] args) {
        TestSharable testSharable = new TestSharable();
        testSharable.test();

    }

    private void test() {
        Class<?> clazz = getClass();
        boolean annotationPresent = clazz.isAnnotationPresent(Sharable.class);
        System.out.println(annotationPresent);
    }
}
