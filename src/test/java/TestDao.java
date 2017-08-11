
import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.apache.shiro.cache.Cache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import samson.common.dao.UserMapper;
import samson.common.po.User;
import samson.core.shiro.cache.JedisManager;
import samson.core.shiro.cache.JedisShiroCache;
import samson.core.shiro.cache.ShiroCacheManager;
import samson.core.spring.*;

import javax.annotation.Resource;

/**
 * Created by 96428 on 2017/7/21.
 * This in ssmjavaconfig, PACKAGE_NAME
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {BaseConfig.class, MybatisConfig.class, MvcConfig.class, CacheConfig.class, ShiroConfig.class})
public class TestDao {
    protected static final Logger LOGGER = Logger.getLogger(TestDao.class);
    @Resource
    UserMapper userMapper;

    @Test
    public void testGet() {
        User user = userMapper.selectByPrimaryKey(1L);

        String deco = org.apache.shiro.codec.Base64.encodeToString("my-application".getBytes());

        LOGGER.info(JSON.toJSONString(user));

        LOGGER.info(new String(org.apache.shiro.codec.Base64.decode(deco)));
    }

    @Resource
    JedisManager jedisManager;

    @Test
    public void test() throws Exception {
        String key = "zhou";
        String value = "历史";
        jedisManager.saveValueByKey(0, key.getBytes(), value.getBytes(), 60);
        LOGGER.info(new String(jedisManager.getValueByKey(0, key.getBytes())));
        jedisManager.deleteByKey(0, key.getBytes());
    }

    @Resource
    ShiroCacheManager shiroCacheManager;

    @Test
    public void te() {
        Cache<Object, Object> cache = shiroCacheManager.getCache("a");

        cache.put("zhou", "ying");
        LOGGER.info(cache.get("zhou"));

        cache.put("zhang", "san");
        LOGGER.info(cache.keys() == null);

        cache.remove("zhou");
        LOGGER.info(cache.get("zhou"));
    }
}
