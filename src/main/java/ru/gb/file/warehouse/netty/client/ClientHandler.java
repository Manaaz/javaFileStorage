package ru.gb.file.warehouse.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.application.Platform;
import ru.gb.file.warehouse.ObjectRegistry;
import ru.gb.file.warehouse.PrimaryController;
import ru.gb.file.warehouse.netty.common.dto.BasicResponse;
import ru.gb.file.warehouse.netty.common.dto.GetFilesListResponse;
import ru.gb.file.warehouse.netty.common.dto.UploadFileResponse;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        BasicResponse response = (BasicResponse) msg;
        PrimaryController primaryController = ObjectRegistry.getInstance(PrimaryController.class);
        if (response instanceof GetFilesListResponse) {
            GetFilesListResponse getFilesListResponse = (GetFilesListResponse) response;
            String dirs = getFilesListResponse.getList()
                    .stream()
                    .map(line -> line + "\n")
                    .reduce(String::concat)
                    .orElse("null");
            Platform.runLater(() -> {
                primaryController.setLabelText(dirs);
            });
        } else if (response instanceof UploadFileResponse) {
            String message = response.getErrorMessage();
            Platform.runLater(() -> {
                primaryController.setLabelText(message);
            });
        } else {
            System.out.println(response.getErrorMessage());
        }
    }
}
