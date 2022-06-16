package alchemystar.freedom.engine.net.proto.mysql;

import alchemystar.freedom.engine.net.proto.MySQLPacket;
import alchemystar.freedom.engine.net.proto.util.BufferUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * EOFPacket
 * https://dev.mysql.com/doc/internals/en/packet-EOF_Packet.html
 *
 * @Author lizhuyang
 */
public class EOFPacket extends MySQLPacket {
    public static final byte FIELD_COUNT = (byte) 0xfe;

    public byte fieldCount = FIELD_COUNT;
    public int warningCount;
    public int status = 2;

    public void read(byte[] data) {
        MySQLMessage mm = new MySQLMessage(data);
        packetLength = mm.readUByte3();
        packetId = mm.read();
        fieldCount = mm.read();
        warningCount = mm.readUByte2();
        status = mm.readUByte2();
    }

    public void read(BinaryPacket bin) {
        packetLength = bin.packetLength;
        packetId = bin.packetId;
        MySQLMessage mm = new MySQLMessage(bin.data);
        fieldCount = mm.read();
        warningCount = mm.readUByte2();
        status = mm.readUByte2();
    }

    @Override
    public ByteBuf writeBuf(ByteBuf buffer) {
        int size = calcPacketSize();
        BufferUtil.writeUByte3(buffer, size);
        buffer.writeByte(packetId);
        buffer.writeByte(fieldCount);
        BufferUtil.writeUByte2(buffer, warningCount);
        BufferUtil.writeUByte2(buffer, status);
        return buffer;
    }

    public boolean hasStatusFlag(long flag) {
        return ((this.status & flag) == flag);
    }

    @Override
    public int calcPacketSize() {
        return 5;// 1+2+2;
    }

    @Override
    protected String getPacketInfo() {
        return "MySQL EOF Packet";
    }

}