package alchemystar.freedom.engine.net.response;

import java.util.ArrayList;
import java.util.List;

import alchemystar.freedom.engine.Database;
import alchemystar.freedom.engine.net.handler.frontend.FrontendConnection;
import alchemystar.freedom.engine.net.proto.mysql.EOFPacket;
import alchemystar.freedom.engine.net.proto.mysql.FieldPacket;
import alchemystar.freedom.engine.net.proto.mysql.ResultSetHeaderPacket;
import alchemystar.freedom.engine.net.proto.mysql.RowDataPacket;
import alchemystar.freedom.engine.net.proto.util.Fields;
import alchemystar.freedom.engine.net.proto.util.PacketUtil;
import alchemystar.freedom.engine.net.proto.util.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * ShowDatabases
 *
 * @Author lizhuyang
 */
public final class ShowDatabases {

    private static final int FIELD_COUNT = 1;
    private static final ResultSetHeaderPacket header = PacketUtil.getHeader(FIELD_COUNT);
    private static final FieldPacket[] fields = new FieldPacket[FIELD_COUNT];
    private static final EOFPacket eof = new EOFPacket();

    static {
        int i = 0;
        byte packetId = 0;
        header.packetId = ++packetId;

        fields[i] = PacketUtil.getField("DATABASE", Fields.FIELD_TYPE_VAR_STRING);
        fields[i++].packetId = ++packetId;

        eof.packetId = ++packetId;
    }

    public static void response(FrontendConnection c) {
        ChannelHandlerContext ctx = c.getCtx();
        ByteBuf buffer = ctx.alloc().buffer();

        // write header
        buffer = header.writeBuf(buffer);

        // write fields
        for (FieldPacket field : fields) {
            buffer = field.writeBuf(buffer);
        }

        // write eof
        buffer = eof.writeBuf(buffer);

        // write rows
        byte packetId = eof.packetId;

        for (String name : getSchemas()) {
            RowDataPacket row = new RowDataPacket(FIELD_COUNT);
            row.add(StringUtil.encode(name, c.getCharset()));
            row.packetId = ++packetId;
            buffer = row.writeBuf(buffer);
        }

        // write lastEof
        EOFPacket lastEof = new EOFPacket();
        lastEof.packetId = ++packetId;
        buffer = lastEof.writeBuf(buffer);

        // write buffer
        ctx.writeAndFlush(buffer);
    }

    private static List<String> getSchemas() {
        Database database = Database.getInstance();
        ArrayList<String> list = new ArrayList<String>();
        // ????????????schema??????
        list.add("freedom");
        return list;
    }
}
