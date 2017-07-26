package samson.core.shiro.token.manager;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import samson.common.po.User;
import samson.common.util.SpringContextUtil;
import samson.core.shiro.session.CustomSessionManager;
import samson.core.shiro.token.MineRealm;
import samson.core.shiro.token.ShiroToken;

import java.util.List;

/**
 * Created by 96428 on 2017/7/25.
 * This in ssmjavaconfig, samson.core.shiro.token.manager
 */
public class TokenManager {

    private static final MineRealm mineRealm = SpringContextUtil.getBean("mineRealm", MineRealm.class);

    private static final CustomSessionManager sessionManager = SpringContextUtil.getBean("customSessionManager", CustomSessionManager.class);

    public static User getToken() {

        return (User) SecurityUtils.getSubject().getPrincipal();
    }

    public static Session getSession() {

        return SecurityUtils.getSubject().getSession();
    }

    public static String getUserName() {

        return getToken().getUserName();
    }

    public static Long getUserId() {

        return getToken().getUserId();
    }

    public static void setDataToSession(Object key, Object value) {

        getSession().setAttribute(key, value);
    }

    public static Object getDataFromSession(Object key) {

        return getSession().getAttribute(key);
    }

    public static String getValidCode() {
        String code = (String) getDataFromSession("validCode");
        getSession().removeAttribute("validCode");

        return code;
    }

    public static User login(User user, boolean rememberMe) {
        ShiroToken token = new ShiroToken(user.getUserName(), user.getPassword());
        token.setRememberMe(rememberMe);
        SecurityUtils.getSubject().login(token);

        return getToken();
    }

    public static boolean isLogin() {

        return SecurityUtils.getSubject().getPrincipal() != null;
    }

    public static void logout() {

        SecurityUtils.getSubject().logout();
    }

    /**
     * 清空当前用户权限信息。
     * 目的：为了在判断权限的时候，再次会再次 <code>doGetAuthorizationInfo(...)  </code>方法。
     * ps：	当然你可以手动调用  <code> doGetAuthorizationInfo(...)  </code>方法。
     * 		这里只是说明下这个逻辑，当你清空了权限，<code> doGetAuthorizationInfo(...)  </code>就会被再次调用。
     */
    public static void clearNowUserAuth(){
        /**
         * 这里需要获取到shrio.xml 配置文件中，对Realm的实例化对象。才能调用到 Realm 父类的方法。
         */
        /**
         * 获取当前系统的Realm的实例化对象，方法一（通过 @link org.apache.shiro.web.mgt.DefaultWebSecurityManager 或者它的实现子类的{Collection<Realm> getRealms()}方法获取）。
         * 获取到的时候是一个集合。Collection<Realm>
         RealmSecurityManager securityManager =
         (RealmSecurityManager) SecurityUtils.getSecurityManager();
         SampleRealm realm = (SampleRealm)securityManager.getRealms().iterator().next();
         */
        /**
         * 方法二、通过ApplicationContext 从Spring容器里获取实列化对象。
         */
        mineRealm.clearCachedAuthorizationInfo();
        /**
         * 当然还有很多直接或者间接的方法，此处不纠结。
         */
    }

    public static void clearUserAuthByUserIds(Long ... userIds) {

        if (userIds == null || userIds.length == 0) return;
        List<SimplePrincipalCollection> principals = sessionManager.getAllSimplePrincipalCollectionByUserIds(userIds);

        for (SimplePrincipalCollection principal : principals) {
            mineRealm.clearCachedAuthorizationInfo(principal);
        }
    }

    public static void clearUserAuthByUserIds(List<Long> userIds) {

        if (userIds == null || userIds.size() == 0) return;

        clearUserAuthByUserIds(userIds.toArray(new Long[0]));
    }
}
