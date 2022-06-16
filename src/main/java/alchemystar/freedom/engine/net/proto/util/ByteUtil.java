package alchemystar.freedom.engine.net.proto.util;

import alchemystar.freedom.engine.net.proto.mysql.MySQLMessage;
import io.netty.buffer.ByteBuf;

/**
 * @author lizhuyang
 */
public class ByteUtil {
    /** int<2> */
    public static int readUByte2(ByteBuf data) {
        int i = data.readByte() & 0xff;
        i |= (data.readByte() & 0xff) << 8;
        return i;
    }

    /** int<3> */
    public static int readUByte3(ByteBuf data) {
        int i = data.readByte() & 0xff;
        i |= (data.readByte() & 0xff) << 8;
        i |= (data.readByte() & 0xff) << 16;
        return i;
    }

    /** int<4> */
    public static long readUByte4(ByteBuf data) {
        long l = data.readByte() & 0xff;
        l |= (data.readByte() & 0xff) << 8;
        l |= (data.readByte() & 0xff) << 16;
        l |= (data.readByte() & 0xff) << 24;
        return l;
    }

    public static long readLong(ByteBuf data) {
        long l = (long) (data.readByte() & 0xff);
        l |= (long) (data.readByte() & 0xff) << 8;
        l |= (long) (data.readByte() & 0xff) << 16;
        l |= (long) (data.readByte() & 0xff) << 24;
        l |= (long) (data.readByte() & 0xff) << 32;
        l |= (long) (data.readByte() & 0xff) << 40;
        l |= (long) (data.readByte() & 0xff) << 48;
        l |= (long) (data.readByte() & 0xff) << 56;
        return l;
    }

    /**
     * this is for the String
     * @param data
     * @return
     */
    public static long readLength(ByteBuf data) {
        int length = data.readByte() & 0xff;
        switch (length) {
            case 251:
                return MySQLMessage.NULL_LENGTH;
            case 252:
                return readUByte2(data);
            case 253:
                return readUByte3(data);
            case 254:
                return readLong(data);
            default:
                return length;
        }
    }


    public static int decodeLength(byte[] src) {
        int length = src.length;
        if (length < 251) {
            return 1 + length;
        } else if (length < 0x10000L) {
            return 3 + length;
        } else if (length < 0x1000000L) {
            return 4 + length;
        } else {
            return 9 + length;
        }
    }

    public static int decodeLength(long length) {
        if (length < 251) {
            return 1;
        } else if (length < 0x10000L) {
            return 3;
        } else if (length < 0x1000000L) {
            return 4;
        } else {
            return 9;
        }
    }

}
