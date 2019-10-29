package io.netty.example.pimow.netty.A20190614;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.concurrent.FastThreadLocalThread;

import java.util.concurrent.atomic.AtomicInteger;

public class FastThreadLocalTest {

    public static final FastThreadLocal<String> local = new FastThreadLocal<>();
    public static final FastThreadLocal<String> local1 = new FastThreadLocal<>();

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            local.set("1");
            local1.set("2");
            local.get();
        });
        FastThreadLocalThread fastThreadLocalThread = new FastThreadLocalThread(thread);
        fastThreadLocalThread.start();

    }

}
