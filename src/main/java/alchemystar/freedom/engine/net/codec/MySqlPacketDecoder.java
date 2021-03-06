package alchemystar.freedom.engine.net.codec;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import alchemystar.freedom.engine.net.proto.mysql.BinaryPacket;
import alchemystar.freedom.engine.net.proto.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * MySqlPacketDecoder
 *
 * @Author lizhuyang
 */
public class MySqlPacketDecoder extends ByteToMessageDecoder {

    private static final Logger logger = LoggerFactory.getLogger(MySqlPacketDecoder.class);

    private final int packetHeaderSize = 4;
    private final int maxPacketSize = 16 * 1024 * 1024;

    /**
     * <a href="https://dev.mysql.com/doc/internals/en/mysql-packet.html">MySql外层结构解包</a>
     *
     * @param ctx
     * @param in
     * @param out
     *
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 4 bytes:3 length + 1 packetId
        if (in.readableBytes() < packetHeaderSize) {
            return;
        }
        in.markReaderIndex();
        int packetLength = ByteUtil.readUByte3(in);
        // 过载保护
        if (packetLength > maxPacketSize) {
            throw new IllegalArgumentException("Packet size over the limit " + maxPacketSize);
        }
        byte packetId = in.readByte();
        if (in.readableBytes() < packetLength) {
            // 半包回溯
            in.resetReaderIndex();
            return;
        }
        BinaryPacket packet = new BinaryPacket();
        packet.packetLength = packetLength;
        packet.packetId = packetId;
        // data will not be accessed any more,so we can use this array safely
        ByteBuf packetBuf = in.readBytes(packetLength);
        packet.data = new byte[packetLength];
        packetBuf.readBytes(packet.data);
        //packet.data = packetBuf.array();
        if (packet.data == null || packet.data.length == 0) {
            logger.error("getDecoder data errorMessage,packetLength=" + packet.packetLength);
        }
        out.add(packet);
    }
}
