//package com.hly.demo.ws.netty;
//
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.Unpooled;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelFutureListener;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.SimpleChannelInboundHandler;
//import io.netty.handler.codec.http.DefaultFullHttpResponse;
//import io.netty.handler.codec.http.FullHttpRequest;
//import io.netty.handler.codec.http.FullHttpResponse;
//import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
//import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
//import io.netty.util.CharsetUtil;
//
//import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
//import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;
//import static io.netty.handler.codec.http.HttpUtil.setContentLength;
//import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
//
//public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
//
//    private WebSocketServerHandshaker handshaker;
//
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
//        if (msg instanceof FullHttpRequest) {
//            handleHttpRequest(ctx, (FullHttpRequest) msg);
//        }
//    }
//
//    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
//
//        // 如果HTTP解码失败，返回http异常
//        if (!req.getDecoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
//            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
//            return;
//        }
//
//        // 构造握手响应返回，目前是本机的地址
//        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://localhost:8080/websocket", null, false);
//        handshaker = wsFactory.newHandshaker(req);
//        if (handshaker == null) {
//            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
//        } else {
//            handshaker.handshake(ctx.channel(), req);
//        }
//    }
//
//    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
//        // 返回应答给客户端
//        if (res.getStatus().code() != 200) {
//            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
//            res.content().writeBytes(buf);
//            buf.release();
//            setContentLength(res, res.content().readableBytes());
//        }
//
//        // 如果是非Keep-Alive，关闭连接
//        ChannelFuture f = ctx.channel().writeAndFlush(res);
//        if (!isKeepAlive(req) || res.getStatus().code() != 200) {
//            f.addListener(ChannelFutureListener.CLOSE);
//        }
//    }
//
//}
