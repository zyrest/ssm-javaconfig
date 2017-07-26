package samson.core.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import samson.common.util.LoggerUtil;
import samson.common.util.SerializeUtil;
import samson.core.shiro.statics.ShiroStatics;

import java.util.Collection;
import java.util.Set;

/**
 * Created by 96428 on 2017/7/21.
 * This in ssmjavaconfig, samson.core.shiro.cache
 */
@SuppressWarnings("unchecked")
public class JedisShiroCache<K, V> implements Cache<K, V> {

    private JedisManager jedisManager;

    private String name;

    static final Class<JedisShiroCache> SELF = JedisShiroCache.class;

    public JedisShiroCache(String name, JedisManager jedisManager) {
        this.name = name;
        this.jedisManager = jedisManager;
    }

    /**
     * 自定义realm中的授权/认证的类名加上授权/认证英文名字
     */
    public String getName() {
        if (name == null) return "";
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String buildCacheKey(Object key) {
        return ShiroStatics.REDIS_SHIRO_CACHE + getName() + ":" + key;
    }

    @Override
    public V get(K k) throws CacheException {
        byte[] keyByte = SerializeUtil.serialize(buildCacheKey(k));
        byte[] valueByte = new byte[0];

        try {
            valueByte = jedisManager.getValueByKey(ShiroStatics.DB_INDEX, keyByte);
        } catch (Exception e) {
            LoggerUtil.fmtError(SELF, "获取value的过程中出现错误", e);
        }

        return (V) SerializeUtil.deserialize(valueByte);
    }

    @Override
    public V put(K k, V v) throws CacheException {
        V pervious = get(k);

        try {
            jedisManager.saveValueByKey(ShiroStatics.DB_INDEX, SerializeUtil.serialize(buildCacheKey(k)),
                    SerializeUtil.serialize(v), -1);
        } catch (Exception e) {
            LoggerUtil.fmtError(SELF, "写入缓存时出错", e);
        }

        return pervious;
    }

    @Override
    public V remove(K k) throws CacheException {
        V pervious = get(k);

        try {
            jedisManager.deleteByKey(ShiroStatics.DB_INDEX, SerializeUtil.serialize(buildCacheKey(k)));
        } catch (Exception e) {
            LoggerUtil.fmtError(SELF, "写入缓存时出错", e);
        }

        return pervious;
    }

    @Override
    public void clear() throws CacheException {
        //TODO
    }

    @Override
    public int size() {
        //TODO
        return 0;
    }

    @Override
    public Set<K> keys() {
        //TODO
        return null;
    }

    @Override
    public Collection<V> values() {
        //TODO
        return null;
    }
}
