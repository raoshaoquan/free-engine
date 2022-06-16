package alchemystar.freedom.engine.net.proto.mysql;

import alchemystar.freedom.engine.net.proto.MySQLPacket;
import alchemystar.freedom.engine.net.proto.util.BufferUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * MySql握手包
 * https://dev.mysql.com/doc/internals/en/connection-phase-packets.html#packet-Protocol::Handshake
 *
 * @Author lizhuyang
 */
public class HandshakePacket extends MySQLPacket {

    private static final byte[] FILLER_13 = new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public byte protocolVersion;
    public byte[] serverVersion;
    public long threadId;
    public byte[] seed;
    public int serverCapabilities;
    public byte serverCharsetIndex;
    public int serverStatus;
    public byte[] restOfScrambleBuff;

    public void read(BinaryPacket bin) {
        packetLength = bin.packetLength;
        packetId = bin.packetId;
        MySQLMessage mm = new MySQLMessage(bin.data);
        protocolVersion = mm.read();
        serverVersion = mm.readBytesWithNull();
        threadId = mm.readUByte4();
        seed = mm.readBytesWithNull();
        serverCapabilities = mm.readUByte2();
        serverCharsetIndex = mm.read();
        serverStatus = mm.readUByte2();
        mm.move(13);
        restOfScrambleBuff = mm.readBytesWithNull();
    }

    @Override
    public void write(final ChannelHandlerContext ctx) {
        // default init 256,so it can avoid buff extract
        final ByteBuf buffer = ctx.alloc().buffer();
        BufferUtil.writeUByte3(buffer, calcPacketSize());
        buffer.writeByte(packetId);
        buffer.writeByte(protocolVersion);
        BufferUtil.writeWithNull(buffer, serverVersion);
        BufferUtil.writeUByte4(buffer, threadId);
        BufferUtil.writeWithNull(buffer, seed);
        BufferUtil.writeUByte2(buffer, serverCapabilities);
        buffer.writeByte(serverCharsetIndex);
        BufferUtil.writeUByte2(buffer, serverStatus);
        buffer.writeBytes(FILLER_13);
        // buffer.position(buffer.position() + 13);
        BufferUtil.writeWithNull(buffer, restOfScrambleBuff);
        // just io , so we don't use thread pool
        ctx.writeAndFlush(buffer);

    }

    @Override
    public int calcPacketSize() {
        int size = 1;
        size += serverVersion.length;// n
        size += 5;// 1+4
        size += seed.length;// 8
        size += 19;// 1+2+1+2+13
        size += restOfScrambleBuff.length;// 12
        size += 1;// 1
        return size;
    }

    @Override
    protected String getPacketInfo() {
        return "MySQL Handshake Packet";
    }
}
