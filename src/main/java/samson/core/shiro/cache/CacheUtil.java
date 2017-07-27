package samson.core.shiro.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import samson.common.util.LoggerUtil;
import samson.common.util.SerializeUtil;
import samson.common.util.SpringContextUtil;

/**
 * Created by 96428 on 2017/7/27.
 * This in ssmjavaconfig, samson.core.shiro.cache
 */
@SuppressWarnings("unchecked")
public class CacheUtil {
    private static final JedisPool jedisPool
            = SpringContextUtil.getBean("jedisPool", JedisPool.class);

    private static final Class CLAZZ = CacheUtil.class;

    public static <T> T get(Integer dbIndex, Object key, Class<T> requireType) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(dbIndex);
            byte[] k = SerializeUtil.serialize(key);
            byte[] v = jedis.get(k);
            return SerializeUtil.deserialize(v, requireType);
        } catch (Exception e) {
            LoggerUtil.fmtError(CLAZZ, e, "获取缓存%s时出错", key);
        } finally {
            returnResource(jedis);
        }

        return null;
    }

    public static void set(Integer dbIndex, Object key, Object value) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(dbIndex);
            byte[] k = SerializeUtil.serialize(key);
            byte[] v = SerializeUtil.serialize(value);
            jedis.set(k, v);
        } catch (Exception e) {
            LoggerUtil.fmtError(CLAZZ, e, "写入缓存失败%s : %s", key, value);
        } finally {
            returnResource(jedis);
        }
    }

    public static void setex(Integer dbIndex, Object key, Object value, Integer exTime) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(dbIndex);
            byte[] k = SerializeUtil.serialize(key);
            byte[] v = SerializeUtil.serialize(value);
            jedis.setex(k, exTime, v);
        } catch (Exception e) {
            LoggerUtil.fmtError(CLAZZ, e, "写入缓存失败%s : %s", key, value);
        } finally {
            returnResource(jedis);
        }
    }

    private static void returnResource(Jedis jedis) {
        if (jedis == null)
            return;
//        if (isBroken)
//            J.getJedisPool().returnBrokenResource(jedis);
//        else
//        	J.getJedisPool().returnResource(jedis);
//        版本问题
        jedis.close();
    }
}
