package samson.core.spring;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by 96428 on 2017/7/21.
 * This in ssmjavaconfig, samson.core.spring
 */
@Component
@Configuration
//加载资源文件
@PropertySource({"classpath:cache.properties"})
//@Lazy
public class CacheConfig {

    private static final Logger LOGGER = Logger.getLogger(CacheConfig.class);
    /**
     * 自动加载环境变量
     * 必须为static
     * @return PropertySourcesPlaceholderConfigurer
     */
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        LOGGER.info("PropertySourcesPlaceholderConfigurer");
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Value("${cache.host}")
    private String host;
    @Value("${cache.port}")
    private int port;
    @Value("${cache.timeout}")
    private int timeout;
    @Value("${cache.password}")
    private String password;

    @Bean
    public JedisPoolConfig jedisPoolConfig() {

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        jedisPoolConfig.setMaxIdle(100);//最大闲置
        jedisPoolConfig.setMinIdle(10);//最小闲置
        jedisPoolConfig.setMaxWaitMillis(5000);//最大等待时间
        jedisPoolConfig.setTestOnBorrow(true);//重新获取

        return jedisPoolConfig;
    }

    @Bean
    public JedisPool jedisPool() {

        return new JedisPool(jedisPoolConfig(), host, port, timeout, password);
    }
}
