import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import samson.common.dao.UserMapper;
import samson.common.po.User;
import samson.common.util.SerializeUtil;
import samson.core.spring.BaseConfig;
import samson.core.spring.MvcConfig;
import samson.core.spring.MybatisConfig;

import javax.annotation.Resource;

/**
 * Created by 96428 on 2017/7/21.
 * This in ssmjavaconfig, PACKAGE_NAME
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {BaseConfig.class, MybatisConfig.class, MvcConfig.class})
public class TestDao {
    protected static final Logger LOGGER = Logger.getLogger(TestDao.class);
    @Resource
    UserMapper userMapper;

    @Test
    public void testGet() {
        User user = userMapper.selectByPrimaryKey(1L);

        String deco = org.apache.shiro.codec.Base64.encodeToString("my-application".getBytes());

        LOGGER.info(deco);

        LOGGER.info(new String(org.apache.shiro.codec.Base64.decode(deco)));
    }

    @Test
    public void test() {
        String line = "[auth]";
        String section = line.replaceFirst("^\\[(\\S+)]$", "$1");

        LOGGER.info(section);
    }
}
