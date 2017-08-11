package samson.core.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import samson.common.util.LoggerUtil;
import samson.core.shiro.session.ShiroSessionRepository;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by 96428 on 2017/7/21.
 * This in ssmjavaconfig, samson.core.shiro
 */

public class CustomShiroSessionDao extends AbstractSessionDAO {

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
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        shiroSessionRepository.saveSession(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        return shiroSessionRepository.getSession(sessionId);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        shiroSessionRepository.saveSession(session);
    }

    @Override
    public void delete(Session session) {
        if (session == null) {
            LoggerUtil.error(getClass(), "session 不能为空");
        }
        Serializable sessiongId = session.getId();
        if (sessiongId != null) {
            shiroSessionRepository.deleteSession(sessiongId);
        }

    }

    @Override
    public Collection<Session> getActiveSessions() {
        return shiroSessionRepository.getAllSessions();
    }
}
