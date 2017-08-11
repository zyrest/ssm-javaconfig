package samson.core.shiro.cache;

import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import samson.common.util.LoggerUtil;
import samson.common.util.SerializeUtil;
import samson.core.shiro.session.SessionStatus;
import samson.core.shiro.session.ShiroSessionRepository;
import samson.core.shiro.statics.ShiroStatics;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by 96428 on 2017/7/21.
 * This in ssmjavaconfig, samson.core.shiro.cache
 */
@SuppressWarnings("unchecked")
public class JedisShiroSessionRepository implements ShiroSessionRepository {

    @Resource
    @Autowired
    private JedisManager jedisManager;
//
//    public JedisManager getJedisManager() {
//        return jedisManager;
//    }
//
//    public void setJedisManager(JedisManager jedisManager) {
//        this.jedisManager = jedisManager;
//    }

    private String buildRedisSessionKey(Serializable sessionId) {
        return ShiroStatics.REDIS_SHIRO_SESSION + sessionId;
    }

    @Override
    public void saveSession(Session session) {
        if (session == null || session.getId() == null) {
            throw new NullPointerException("session is empty");
        }

        try {
            byte[] key = SerializeUtil.serialize(buildRedisSessionKey(session.getId()));

            //不存在才添加
            if (session.getAttribute(ShiroStatics.SESSION_STATUS)  == null) {
                SessionStatus sessionStatus = new SessionStatus();
                session.setAttribute(ShiroStatics.SESSION_STATUS, sessionStatus);
            }

            byte[] value = SerializeUtil.serialize(session);
            long sessionTimeOut = session.getTimeout() / 1000;
            Long expireTime = sessionTimeOut + ShiroStatics.SESSION_VAL_TIME_SPAN + (5 * 60);
            jedisManager.saveValueByKey(ShiroStatics.DB_INDEX, key, value, expireTime.intValue());
        } catch (Exception e) {
            LoggerUtil.fmtError(getClass(), e, "存储session出错 ： id是%s", session.getId());
        }

    }

    @Override
    public void deleteSession(Serializable sessionId) {
        if (sessionId == null) {
            throw new NullPointerException("session id is empty");
        }

        byte[] key = SerializeUtil.serialize(buildRedisSessionKey(sessionId));

        try {
            jedisManager.deleteByKey(ShiroStatics.DB_INDEX, key);
        } catch (Exception e) {
            LoggerUtil.fmtError(getClass(), e, "删除session出错 ： id是%s", sessionId);
        }

    }

    @Override
    public Session getSession(Serializable sessionId) {
        if (sessionId == null) {
            throw new NullPointerException("session id is empty");
        }

        byte[] key = SerializeUtil.serialize(buildRedisSessionKey(sessionId));
        byte[] value = new byte[0];
        Session session = null;

        try {
            value = jedisManager.getValueByKey(ShiroStatics.DB_INDEX, key);
            session = SerializeUtil.deserialize(value, Session.class);
        } catch (Exception e) {
            LoggerUtil.fmtError(getClass(), e, "获取session出错 ： id是%s", sessionId);
        }

        return session;
    }

    @Override
    public Collection<Session> getAllSessions() {
        Collection<Session> collection = null;

        try {
            collection = jedisManager.getAllSessions(ShiroStatics.DB_INDEX);
        } catch (Exception e) {
            LoggerUtil.fmtError(getClass(), e, "获取全部session出错");
        }

        return collection;
    }
}
