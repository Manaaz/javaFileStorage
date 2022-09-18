package ru.gb.file.warehouse.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.gb.file.warehouse.ObjectRegistry;
import ru.gb.file.warehouse.netty.common.AuthService;
import ru.gb.file.warehouse.netty.common.dto.BasicRequest;
import ru.gb.file.warehouse.netty.common.dto.BasicResponse;
import ru.gb.file.warehouse.netty.common.dto.RegisterUserRequest;
import ru.gb.file.warehouse.netty.common.handler.HandlerRegistry;
import ru.gb.file.warehouse.netty.common.handler.RequestHandler;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println(ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) {
        BasicRequest request = (BasicRequest) msg;
        AuthService authService = ObjectRegistry.getInstance(AuthService.class);
        String authToken = request.getAuthToken();
        if (!(request instanceof RegisterUserRequest) && !authService.auth(authToken)) {
            BasicResponse authErrorResponse = new BasicResponse("Not authenticated!");
            channelHandlerContext.writeAndFlush(authErrorResponse);
        }

        RequestHandler handler = HandlerRegistry.getHandler(request.getClass());
        BasicResponse response = handler.handle(request, channelHandlerContext);
        channelHandlerContext.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }

}
