package samson.core.shiro.session;

import org.apache.shiro.session.Session;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by 96428 on 2017/7/21.
 * This in ssmjavaconfig, samson.core.shiro.session
 */
public interface ShiroSessionDao {
    /**
     * 存储Session
     * @param session
     */
    void saveSession(Session session);
    /**
     * 删除session
     * @param sessionId
     */
    void deleteSession(Serializable sessionId);
    /**
     * 获取session
     * @param sessionId
     * @return
     */
    Session getSession(Serializable sessionId);
    /**
     * 获取所有sessoin
     * @return
     */
    Collection<Session> getAllSessions();
}
