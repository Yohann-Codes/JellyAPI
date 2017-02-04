package transport;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 心跳Handler
 *
 * @author Yohann.
 */
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {
    private HeartbeatManager heartbeatManager;

    public HeartbeatHandler(HeartbeatManager heartbeatManager) {
        this.heartbeatManager = heartbeatManager;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            // 向服务器发送心跳包
            heartbeatManager.heartbeat();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        heartbeatManager.inActive();
    }
}
