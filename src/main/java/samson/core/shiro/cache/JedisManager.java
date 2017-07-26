package samson.core.shiro.cache;

import org.apache.shiro.session.Session;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import samson.common.util.LoggerUtil;
import samson.common.util.SerializeUtil;
import samson.core.shiro.statics.ShiroStatics;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by 96428 on 2017/7/21.
 * This in ssmjavaconfig, samson.core.shiro.cache
 * session 创建、删除、查询
 */
@Component
public class JedisManager {

    @Resource
    private JedisPool jedisPool;

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    /**
     * 获取Jedis
     * @return 当前系统的Jedis
     */
    public Jedis getJedis() {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
        } catch (JedisConnectionException e) {
            System.out.println("jedis 启动失败 请检查配置");
            e.printStackTrace();
            System.exit(0);
        }

        return jedis;
    }

    /**
     * 归还资源
     * @param jedis 需要归还的jedis
     */
    public void returnResource(Jedis jedis) {
        if (jedis == null) return;
        jedis.close();
    }

    public byte[] getValueByKey(int dbIndex, byte[] key) throws Exception {
        Jedis jedis = null;
        byte[] result = null;

        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            result = jedis.get(key);
        } finally {
            returnResource(jedis);
        }

        return result;
    }

    public void deleteByKey(int dbIndex, byte[] key) throws Exception {
        Jedis jedis = null;

        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            long result = jedis.del(key);
            LoggerUtil.fmtDebug(getClass(), "正在删除, 结果为%s", result);
        } finally {
            returnResource(jedis);
        }
    }

    public void saveValueByKey(int dbIndex, byte[] key, byte[] value, int expireTime) throws Exception {
        Jedis jedis = null;

        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            jedis.set(key, value);
            if (expireTime > 0) {
                jedis.expire(key, expireTime);
            }
        } finally {
            returnResource(jedis);
        }
    }


    /**
     * 取出所有session
     * @param dbIndex 数据库索引
     * @return        当前所有sessions
     * @throws Exception  可能出错
     */
    @SuppressWarnings("unchecked")
    public Collection<Session> getAllSessions(int dbIndex) throws Exception {
        Jedis jedis = null;
        Set<Session> sessions = new HashSet<>();

        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            Set<byte[]> all = jedis.keys(ShiroStatics.REDIS_SHIRO_ALL.getBytes());

            if (all != null && all.size() > 0) {
                for (byte[] one : all) {
                    Session got = SerializeUtil.deserialize(jedis.get(one), Session.class);
                    if (got instanceof Session) {
                        sessions.add(got);
                    }
                }
            }

        } finally {
            returnResource(jedis);
        }

        return sessions;
    }

}
