package ru.gb.file.warehouse.netty.common.handler;

import io.netty.channel.ChannelHandlerContext;
import ru.gb.file.warehouse.netty.common.dto.BasicRequest;
import ru.gb.file.warehouse.netty.common.dto.BasicResponse;

public interface RequestHandler<REQUEST extends BasicRequest, RESPONSE extends BasicResponse> {

    RESPONSE handle(REQUEST request, ChannelHandlerContext channelHandlerContext);
}