package samson.core.shiro.statics;

/**
 * Created by 96428 on 2017/7/22.
 * This in ssmjavaconfig, samson.core.shiro.cache
 */
public class ShiroStatics {
    /**
     * 为了不和其他的缓存混淆，采用追加前缀方式以作区分
     */
    public static final String REDIS_SHIRO_CACHE = "my-application-cache:";
    /**
     * Redis 分片(分区)，也可以在配置文件中配置
     */
    public static final int DB_INDEX = 1;
    public static final int CACHE_INDEX = 0;

    public static final String REDIS_SHIRO_SESSION = "my-application-session:";
    //这里有个小BUG，因为Redis使用序列化后，Key反序列化回来发现前面有一段乱码，解决的办法是存储缓存不序列化
    public static final String REDIS_SHIRO_ALL = "*my-application-session:*";

    public static final int SESSION_VAL_TIME_SPAN = 18000;
    /**
     * session status
     */
    public static final String SESSION_STATUS ="application-online-status";
}
