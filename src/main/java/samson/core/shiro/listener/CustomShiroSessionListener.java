package samson.core.shiro.listener;

import org.apache.log4j.Logger;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.springframework.beans.factory.annotation.Autowired;
import samson.core.shiro.session.ShiroSessionRepository;

import javax.annotation.Resource;

/**
 * Created by 96428 on 2017/7/22.
 * This in ssmjavaconfig, samson.core.shiro.listener
 */
public class CustomShiroSessionListener implements SessionListener {

    private Logger logger = Logger.getLogger(CustomShiroSessionListener.class);

    @Resource
    @Autowired
    private ShiroSessionRepository shiroSessionRepository;

//    public ShiroSessionRepository getShiroSessionRepository() {
//        return shiroSessionRepository;
//    }
//
//    public void setShiroSessionRepository(ShiroSessionRepository shiroSessionRepository) {
//        this.shiroSessionRepository = shiroSessionRepository;
//    }

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
        shiroSessionRepository.deleteSession(session.getId());
    }


}
