package ir.chat;


import ir.chat.util.RSAEncrypt;
import org.omg.CORBA.TIMEOUT;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Mohammad Amin on 20/09/2015.
 */
public class Server implements Runnable {

    public final static String ADDRESS = "localhost";
    public final static int PORT = 5000;
    public final static long TIMEOUT = 10000;
    private ServerSocketChannel serverChannel;
    private Selector selector;
    private Map<SocketChannel, byte[]> dataTracking = new HashMap<SocketChannel, byte[]>();

    public Server() {
        init();
    }

    private void init() {
        System.out.println("initialize login port ... ");
        if (selector != null) return;
        if (serverChannel != null) return;

        try {
            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.socket().bind(new InetSocketAddress(ADDRESS, PORT));
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Now accepting connections...");
        try {
            while (!Thread.currentThread().isInterrupted()) {
                selector.select(TIMEOUT);
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();
                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isAcceptable()) {
                        System.out.println("Accepting connection");
                        accept(key);
                    }
                    if (key.isWritable()) {
                        System.out.println("Writing...");
                        write(key);
                    }
                    if (key.isReadable()) {
                        System.out.println("Reading connection");
                        read(key);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        byte[] data = dataTracking.get(channel);
        dataTracking.remove(channel);
        channel.write(ByteBuffer.wrap(data));
        key.interestOps(SelectionKey.OP_READ);

    }

    private void closeConnection() {
        System.out.println("Closing server down");
        if (selector != null) {
            try {
                selector.close();
                serverChannel.socket().close();
                serverChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_WRITE);
        byte[] serverPublicKey = RSAEncrypt.getPublicKey().getBytes();
        dataTracking.put(socketChannel, serverPublicKey);
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        readBuffer.clear();
        int read;
        try {
            read = channel.read(readBuffer);
        } catch (IOException e) {
            System.out.println("Reading problem, closing connection");
            key.cancel();
            channel.close();
            return;
        }
        if (read == -1) {
            System.out.println("Nothing was there to be read, closing connection");
            channel.close();
            key.cancel();
            return;
        }
        // IMPORTANT - don't forget the flip() the buffer. It is like a reset without clearing it.
        readBuffer.flip();
        byte[] data = new byte[1000];
        readBuffer.get(data, 0, read);
        System.out.println("Received: " + new String(data));

        echo(key, data);
    }

    private void echo(SelectionKey key, byte[] data) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        dataTracking.put(socketChannel, data);
        key.interestOps(SelectionKey.OP_WRITE);
    }

}