package com.ysheep.io.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * @author ysheep
 * @version 1.0.0
 * @Description 自定义服务端处理器,对客户端的数据进行处理的处理器
 * @createTime 2020/06/07/  15:51:00
 */
public class SomeServerHandler extends ChannelInboundHandlerAdapter {

    /**
     *
     * @param ctx 上下文
     * @param msg 来自客户端的信息 请求的数据
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "," + msg);
//        ctx.channel().writeAndFlush("from servert:" + UUID.randomUUID());
//        TimeUnit.SECONDS.sleep(1);
        if(msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            System.out.println("请求方式:" + request.getMethod().name());
            System.out.println("请求的uri" + request.getUri());
            // 浏览器的图标
            if ("/favicon.ico".equalsIgnoreCase(request.getUri())) {
                System.out.println("不处理/favicon.ico请求");
                return;
            }
            // 响应体
            ByteBuf buffer = Unpooled.copiedBuffer("hello netty world", CharsetUtil.UTF_8);
            // 生成相应对象
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,buffer);
            // 获取到response的头部进行初始化
            HttpHeaders headers = response.headers();
            headers.set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            headers.set(HttpHeaderNames.CONTENT_LENGTH, buffer.readableBytes());
//            ctx.write(response);
//            ctx.flush();
            ctx.writeAndFlush(response)
                    //  添加监听器，响应体发送完完毕直接将channel关闭
                    .addListener(ChannelFutureListener.CLOSE);

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 关闭Channel
        ctx.close();
    }
}
