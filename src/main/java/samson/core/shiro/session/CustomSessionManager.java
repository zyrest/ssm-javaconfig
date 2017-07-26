package samson.core.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.stereotype.Component;
import samson.common.po.User;
import samson.common.util.ArrayUtil;
import samson.common.util.LoggerUtil;
import samson.core.shiro.CustomShiroSessionDao;
import samson.core.shiro.statics.ShiroStatics;
import samson.user.bo.UserOnlineBo;

import javax.annotation.Resource;
import java.util.*;

import static samson.core.shiro.statics.ShiroStatics.SESSION_STATUS;

/**
 * Created by 96428 on 2017/7/21.
 * This in ssmjavaconfig, samson.core.shiro.session
 */
@Component
public class CustomSessionManager {

    @Resource
    private ShiroSessionDao shiroSessionDao;

    @Resource
    private CustomShiroSessionDao customShiroSessionDao;

    /**
     * 获取当前系统内所有用户的集合
     * @return 用户集合
     */
    public List<UserOnlineBo> getAllUsers() {
        Collection<Session> sessions = shiroSessionDao.getAllSessions();
        List<UserOnlineBo> users = new ArrayList<>();

        for (Session session : sessions) {
            UserOnlineBo tmp = getUserFromSession(session);
            if (tmp != null) {
                users.add(tmp);
            }
        }

        return users;
    }

    public List<SimplePrincipalCollection> getAllSimplePrincipalCollectionByUserIds(Long ... userIds) {
        //转化array为set 利于使用
        Set<Long> ids = ArrayUtil.arrays2Set(userIds);
        //获取所有session、
        Collection<Session> sessions = customShiroSessionDao.getActiveSessions();

        List<SimplePrincipalCollection> ans = new ArrayList<>();

        //分别判断每一个session里的用户的id是否在ids里
        //若在ids内则添加到结果list中
        for (Session session : sessions) {
            //获取spc
            Object rawty = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);

            if (rawty != null && rawty instanceof SimplePrincipalCollection) {
                SimplePrincipalCollection principalCollection = (SimplePrincipalCollection) rawty;

                //判断用户
                //通过getPrimaryPrincipal()可以获取用户实体
                rawty = principalCollection.getPrimaryPrincipal();
                if (rawty != null && rawty instanceof User) {
                    User user = (User) rawty;
                    //若用户id在ids内则加入
                    if (ids.contains(user.getUserId())) {
                        ans.add(principalCollection);
                    }
                }

            }
        }

        return ans;
    }

    public UserOnlineBo getSession(String sessionId) {
        Session session = shiroSessionDao.getSession(sessionId);
        return getUserFromSession(session);
    }

    private UserOnlineBo getUserFromSession(Session session) {

        Object object = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        if (object == null) {
            return null;
        }

        //存储session和user的所有信息
        UserOnlineBo user = null;

        if (object instanceof SimplePrincipalCollection) {
            SimplePrincipalCollection principal = (SimplePrincipalCollection) object;
            /*
             * 获取用户登录的，@link SampleRealm.doGetAuthenticationInfo(...)方法中
             * return new SimpleAuthenticationInfo(user,user.getPswd(), getName());的user 对象。
             */
            object = principal.getPrimaryPrincipal();
            if (object != null && object instanceof User) {
                user = new UserOnlineBo((User) object);

                user.setHost(session.getHost());
                user.setLastAccess(session.getLastAccessTime());
                user.setSessionId(session.getId().toString());
                user.setStartTime(session.getStartTimestamp());
                user.setTimeout(session.getTimeout());//回话到期 ttl(ms)

                //是否踢出
                SessionStatus status = (SessionStatus) session.getAttribute(SESSION_STATUS);
                boolean st = true;
                if (status != null) {
                    st = status.getOnlineStatus();
                }
                user.setSessionStatus(st);
            }
        }

        return user;
    }

    /**
     *
     * @param sessionIds 需要改变的sessionIds(用,间隔）
     * @param status 改变后的状态{}
     * @return
     */
    public Map<String, Object> changeSessionStatus(String sessionIds, Boolean status) {
        Map<String, Object> ans = new HashMap<>();

        try {
            String[] ids = sessionIds.split(",");

            for (String id : ids ) {
                Session session = shiroSessionDao.getSession(id);
                SessionStatus sessionStatus = new SessionStatus();
                sessionStatus.setOnlineStatus(status);
                session.setAttribute(SESSION_STATUS, sessionStatus);
                customShiroSessionDao.update(session);
            }

            ans.put("status", 200);
            ans.put("sessionStatus", status?1:0);
            ans.put("sessionStatusText", status?"踢出":"激活");
            ans.put("sessionStatusTextTd", status?"有效":"已踢出");
        } catch (Exception e) {
            ans.put("status", 200);
            ans.put("message", "session 可能不存在 检查session id");
            LoggerUtil.fmtError(getClass(), e, "请检查session id: %s", sessionIds);
        }

        return ans;
    }

    /**
     * 查询要禁用的用户是否在线。
     * @param id		用户ID
     * @param status	用户状态
     */
    public void forbidUserById(Long id, Long status) {
        //获取所有在线用户
        for(UserOnlineBo bo : getAllUsers()){
            Long userId = bo.getUserId();
            //匹配用户ID
            if(userId.equals(id)){
                //获取用户Session
                Session session = shiroSessionDao.getSession(bo.getSessionId());
                //标记用户Session
                SessionStatus sessionStatus = (SessionStatus) session.getAttribute(ShiroStatics.SESSION_STATUS);
                //是否踢出 true:有效，false：踢出。
                sessionStatus.setOnlineStatus(status.intValue() == 1);
                //更新Session
                customShiroSessionDao.update(session);
            }
        }
    }
}
