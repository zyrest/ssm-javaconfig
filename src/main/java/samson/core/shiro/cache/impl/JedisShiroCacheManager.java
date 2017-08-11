package samson.core.shiro.cache.impl;

import org.apache.shiro.cache.Cache;
import samson.core.shiro.cache.JedisManager;
import samson.core.shiro.cache.JedisShiroCache;
import samson.core.shiro.cache.ShiroCacheManager;

import javax.annotation.Resource;

/**
 * Created by 96428 on 2017/7/21.
 * This in ssmjavaconfig, samson.core.shiro.cache.impl
 */
public class JedisShiroCacheManager implements ShiroCacheManager{

    @Resource
    private JedisManager jedisManager;

    public JedisShiroCacheManager() {
        System.out.println();
        System.out.println("求你了 @ " + jedisManager);
        System.out.println();
    }

//    public JedisManager getJedisManager() {
//        return jedisManager;
//    }
//
//    public void setJedisManager(JedisManager jedisManager) {
//        this.jedisManager = jedisManager;
//    }


    public <K, V> Cache<K, V> getCache(String name) {

        return new JedisShiroCache<>(name, jedisManager);
    }

    public void destroy() {
        //如果和其他系统，或者应用在一起就不能关闭
        //getJedisManager().getJedis().shutdown();
    }


}
