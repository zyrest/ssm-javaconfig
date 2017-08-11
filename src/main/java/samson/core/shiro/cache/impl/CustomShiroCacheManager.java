package samson.core.shiro.cache.impl;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.Destroyable;
import org.springframework.beans.factory.annotation.Autowired;
import samson.core.shiro.cache.ShiroCacheManager;

/**
 * Created by 96428 on 2017/7/21.
 * This in ssmjavaconfig, samson.core.shiro.cache.impl
 */

public class CustomShiroCacheManager implements Destroyable, CacheManager {

    @Autowired
    private ShiroCacheManager shiroCacheManager;

//    public ShiroCacheManager getShiroCacheManager() {
//
//        return shiroCacheManager;
//    }
//
//    public void setShiroCacheManager(ShiroCacheManager shiroCacheManager) {
//
//        this.shiroCacheManager = shiroCacheManager;
//    }

    @Override
    public void destroy() throws Exception {

        shiroCacheManager.destroy();
    }

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {

        System.out.println();
        System.out.println(name + " @ " + shiroCacheManager);
        System.out.println();

        return shiroCacheManager.getCache(name);
    }
}
