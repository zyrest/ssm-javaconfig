package samson.core.shiro.listener;

import org.apache.log4j.Logger;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.springframework.stereotype.Component;
import samson.core.shiro.session.ShiroSessionDao;

import javax.annotation.Resource;

/**
 * Created by 96428 on 2017/7/22.
 * This in ssmjavaconfig, samson.core.shiro.listener
 */
@Component
public class CustomShiroSessionListener implements SessionListener{

    private Logger logger = Logger.getLogger(CustomShiroSessionListener.class);

    @Resource
    private ShiroSessionDao shiroSessionDao;

    public ShiroSessionDao getShiroSessionDao() {
        return shiroSessionDao;
    }

    public void setShiroSessionDao(ShiroSessionDao shiroSessionDao) {
        this.shiroSessionDao = shiroSessionDao;
    }

    @Override
    public void onStart(Session session) {
        logger.info("session listener on start");
        //TODO
    }

    @Override
    public void onStop(Session session) {
        logger.info("session listener on stop");
        //TODO
    }

    @Override
    public void onExpiration(Session session) {
        shiroSessionDao.deleteSession(session.getId());
    }


}
