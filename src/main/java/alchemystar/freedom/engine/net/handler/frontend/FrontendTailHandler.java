package alchemystar.freedom.engine.net.handler.frontend;

import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import alchemystar.freedom.engine.net.proto.util.ErrorCode;
import io.netty.channel.ChannelHandlerContext;

/**
 * TailHandler 做exception的操作
 *
 * @Author lizhuyang
 */
public class FrontendTailHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(FrontendTailHandler.class);

    protected FrontendConnection source;

    public FrontendTailHandler(FrontendConnection source) {
        this.source = source;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Exception caught", cause);
        FrontendGroupHandler.frontendGroup.remove(source.getId());
        source.writeErrMessage(ErrorCode.ERR_EXCEPTION_CAUGHT, cause.getMessage());
    }

}
