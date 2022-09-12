package ru.gb.file.warehouse;

import ru.gb.file.warehouse.netty.client.NettyClient;
import ru.gb.file.warehouse.netty.client.service.UploadFileService;
import ru.gb.file.warehouse.netty.common.AuthService;

import java.util.HashMap;
import java.util.Map;

public class ObjectRegistry {

    private static final Map<Class<?>, Object> INSTANCE_REGISTRY = new HashMap<>();

    static {
        NettyClient nettyClient = new NettyClient();
        reg(NettyClient.class, nettyClient);

        UploadFileService uploadFileService = new UploadFileService();
        reg(UploadFileService.class, uploadFileService);

        AuthService authService = new AuthService();
        reg(AuthService.class, authService);
    }

    public static void reg(Class<?> clazz, Object instance) {
        INSTANCE_REGISTRY.put(clazz, instance);
    }

    public static <T> T getInstance(Class<T> clazz) {
        return (T) INSTANCE_REGISTRY.get(clazz);
    }
}
