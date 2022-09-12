package ru.gb.file.warehouse.netty.client.service;

import ru.gb.file.warehouse.ObjectRegistry;
import ru.gb.file.warehouse.netty.client.NettyClient;
import ru.gb.file.warehouse.netty.common.FileSaw;
import ru.gb.file.warehouse.netty.common.dto.UploadFileRequest;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import static ru.gb.file.warehouse.netty.client.NettyClient.TOKEN;

public class UploadFileService {

    public void uploadFile(String pathToFile) {
        Path filePath = Paths.get(pathToFile);
        NettyClient nettyClient = ObjectRegistry.getInstance(NettyClient.class);
        String fileName = filePath.getFileName().toString();
        Consumer<byte[]> filePartConsumer = filePartBytes -> {
            UploadFileRequest uploadFileRequest = new UploadFileRequest(TOKEN, fileName, filePartBytes);
            nettyClient.uploadFile(uploadFileRequest);
        };

        FileSaw fileSaw = new FileSaw();
        fileSaw.saw(filePath, filePartConsumer);
    }
}
