package ru.gb.file.warehouse.netty.common.handler;

import ru.gb.file.warehouse.netty.common.dto.BasicRequest;
import ru.gb.file.warehouse.netty.common.dto.GetFilesListRequest;
import ru.gb.file.warehouse.netty.common.dto.RegisterUserRequest;
import ru.gb.file.warehouse.netty.common.dto.UploadFileRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HandlerRegistry {

    private static final Map<Class<? extends BasicRequest>, RequestHandler<?, ?>> HANDLERS_MAP;

    static {
        Map<Class<? extends BasicRequest>, RequestHandler<?, ?>> handlerMap = new HashMap<>();
        handlerMap.put(GetFilesListRequest.class, new GetFilesListHandler());
        handlerMap.put(RegisterUserRequest.class, new RegisterUserHandler());
        handlerMap.put(UploadFileRequest.class, new UploadFileHandler());

        HANDLERS_MAP = Collections.unmodifiableMap(handlerMap);
    }

    public static RequestHandler<?, ?> getHandler(Class<? extends BasicRequest> request) {
        return HANDLERS_MAP.get(request);
    }

}
