package samson.core.shiro.cache;

import org.apache.shiro.cache.Cache;

/**
 * Created by 96428 on 2017/7/21.
 * This in ssmjavaconfig, samson.core.shiro.cache
 */

public interface ShiroCacheManager {
    <K, V> Cache<K, V> getCache(String name);

    void destroy();
}
