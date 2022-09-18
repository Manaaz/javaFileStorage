import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

class ServerTest {

    private static final String FILE_PARAMS_TPL = "";

    private static final Properties PROPERTIES;

    static {
        try (InputStream fileInputStream = ServerTest.class.getResourceAsStream("/client.properties")) {
            Properties properties = new Properties();
            properties.load(fileInputStream);
            PROPERTIES = properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testServer() throws IOException {
        List<Socket> sockets = new ArrayList<>();
        for (int i = 0; i < 1500; i++) {
            Socket clientSocket = new Socket("localhost", 45001);
            sockets.add(clientSocket);
        }
        System.out.println(sockets);
    }

    @Test
    // * Подключаюсь к серверу
    // * Передаю строку с мета информацией "ACTION=upload&FILE_NAME=somefilename.txt&USERNAME=Bogdan&PASS=qwerty "
    // * Передаю строку с мета информацией "ACTION=download&FILE_PATH=/path/to/server/file/somefilename.txt&USERNAME=Bogdan&PASS=qwerty "
    // * Разделитель " "
    void sendFileToServerWithIo() throws IOException {
        Socket clientSocket = new Socket("localhost", 45001);
        File file = new File(PROPERTIES.getProperty("rootPath") + "/client-dir/img.png");
        try (FileInputStream fileInputStream = new FileInputStream(file);
             OutputStream outputStream = clientSocket.getOutputStream()) {
            String metaInfo = String.format("ACTION=upload&FILE_NAME=%s&USERNAME=Bogdan&PASS=qwerty ", file.getName());
            byte[] metadataBytes = metaInfo.getBytes(StandardCharsets.UTF_8);
            outputStream.write(metadataBytes);
            fileInputStream.transferTo(outputStream);
        }
    }

    @Test
    void sendFileToServerWithNio() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 45001));
        socketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_WRITE);

        selector.select();

        ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
        RandomAccessFile fileForSend = new RandomAccessFile(PROPERTIES.getProperty("rootPath") + "/client-dir/img.png", "rw");
        FileChannel fileChannel = fileForSend.getChannel();
        byte[] metadataBytes = String.format(FILE_PARAMS_TPL, "img.png").getBytes(StandardCharsets.UTF_8);
        byteBuffer.put(metadataBytes);

        for (SelectionKey selectionKey : selector.selectedKeys()) {
            if (selectionKey.isValid() && selectionKey.isWritable()) {
                while (fileChannel.read(byteBuffer) != -1) {
                    socketChannel.write(byteBuffer);
                    byteBuffer.clear();
                }
            }
        }
    }
}