package transport;

import dispatch.Dispatcher;
import api.future.ConnectionListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 最终接收数据的Handler.
 *
 * @author Yohann.
 */
public class AcceptorHandler extends ChannelInboundHandlerAdapter {
    private ConnectionListener connListener;

    public AcceptorHandler(ConnectionListener connListener) {
        this.connListener = connListener;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        connListener.onConnetionComplete(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof MessageHolder) {
            MessageHolder messageHolder = (MessageHolder) msg;
            // 处理消息
            Dispatcher.dispatch(messageHolder);
        } else {
            throw new IllegalArgumentException("msg is not instance of MessageHolder");
        }
    }
}
