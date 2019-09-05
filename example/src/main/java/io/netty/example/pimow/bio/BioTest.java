package io.netty.example.pimow.bio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author: Pimow
 **/
public class BioTest {

    public static void main(String[] args) throws Exception {
        read("/Users/mujourney/Desktop/dump");
    }

    private static void read(String s) throws IOException {
        RandomAccessFile rw = new RandomAccessFile(s, "rw");
        FileChannel channel = rw.getChannel();
        channel.read(new ByteBuffer[1]);

        InputStreamReader isr = new InputStreamReader(new FileInputStream(s), "UTF-8");
        BufferedReader read = new BufferedReader(isr);
        int i = 0;
        StringBuilder builder = new StringBuilder();
        while ((i = read.read()) != -1) {
            builder.append((char) i);
        }
        System.out.println(builder.toString());

    }

    private static void write(String s) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(s));
//            FileOutputStream fileOutputStream = new FileOutputStream(s);
//            fileOutputStream.write("我爱你".getBytes());
            bufferedWriter.write("我放屁啦");
            bufferedWriter.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
