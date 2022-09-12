package ru.gb.file.warehouse.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import ru.gb.file.warehouse.netty.common.dto.GetFilesListRequest;
import ru.gb.file.warehouse.netty.common.dto.UploadFileRequest;

public class NettyClient {

    public static final int MAX_OBJECT_SIZE = 20 * 1_000_000;
    public static final String TOKEN = "Bogdan:1234";

    private final Channel clientChannel;

    public NettyClient() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .remoteAddress("localhost", 45001)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline().addLast(
                                new ObjectDecoder(MAX_OBJECT_SIZE, ClassResolvers.cacheDisabled(null)),
                                new ObjectEncoder(),
                                new ClientHandler()
                        );
                    }
                });

        ChannelFuture channelFuture = bootstrap.connect();
        this.clientChannel = channelFuture.channel();
    }

    public void getFilesList() {
        GetFilesListRequest getFilesListRequest = new GetFilesListRequest(TOKEN, "/");
        clientChannel.writeAndFlush(getFilesListRequest);
    }

    public void uploadFile(UploadFileRequest uploadFileRequest) {
        try {
            clientChannel.writeAndFlush(uploadFileRequest).sync();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
