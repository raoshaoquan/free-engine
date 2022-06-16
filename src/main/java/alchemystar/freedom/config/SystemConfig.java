package alchemystar.freedom.config;

import alchemystar.freedom.engine.net.proto.util.Isolations;

/**
 * SystemConfig
 *
 * @Author lizhuyang
 */
public class SystemConfig {

    //TODO: 不同表,不同文件夹路径

    public static final int DEFAULT_PAGE_SIZE = 4096;

    public static final int DEFAULT_SPECIAL_POINT_LENGTH = 64;

    /** 工作目录 */
    public static final String RELATION_FILE_PRE_FIX = System.getProperty("user.dir") + "/tmp";

    /** 数据目录 */
    public static final String FREEDOM_REL_DATA_PATH = RELATION_FILE_PRE_FIX + "/data";

    /** 索引目录 */
    public static final String FREEDOM_REL_IDX_PATH = RELATION_FILE_PRE_FIX + "/idx";

    /** 数据目录 */
    public static final String FREEDOM_REL_META_PATH = RELATION_FILE_PRE_FIX + "/meta";

    /** binlog */
    public static final String FREEDOM_LOG_FILE_NAME = RELATION_FILE_PRE_FIX + "/log/bin.log";

    public static final String Database = "";
    // 36小时内连接不发起请求就干掉 秒为单位
    // long IDLE_TIME_OUT = 36 * 3600 * 1000;
    public static final long IDLE_TIME_OUT = 36 * 3600;

    // 1小时做一次idle check 秒为单位
    //int IDLE_CHECK_INTERVAL = 3600 * 1000;
    public static final int IDLE_CHECK_INTERVAL = 3600;

    public static final String DEFAULT_CHARSET = "utf8";

    public static final int DEFAULT_TX_ISOLATION = Isolations.REPEATED_READ;
}
