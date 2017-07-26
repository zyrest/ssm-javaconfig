package samson.core.spring;

import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import samson.core.shiro.CustomShiroSessionDao;
import samson.core.shiro.cache.impl.CustomShiroCacheManager;
import samson.core.shiro.filter.*;
import samson.core.shiro.listener.CustomShiroSessionListener;
import samson.core.shiro.service.ShiroGetChain;
import samson.core.shiro.session.ShiroSessionDao;
import samson.core.shiro.token.MineRealm;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 96428 on 2017/7/21.
 * This in ssmjavaconfig, samson.core.spring
 */
@Configuration
public class ShiroConfig {

    /**
     * 会话验证调度器
     * @return ExecutorServiceSessionValidationScheduler
     */
    @Bean
    public ExecutorServiceSessionValidationScheduler sessionValidationScheduler() {

        ExecutorServiceSessionValidationScheduler sessionValidationScheduler = new ExecutorServiceSessionValidationScheduler();
        sessionValidationScheduler.setInterval(1000 * 60 * 60 * 5);
//        sessionValidationScheduler.setSessionManager(sessionManager);
        return sessionValidationScheduler;
    }

    /**
     * Session Manager
     * @param customShiroSessionDao
     * @param customShiroSessionListener
     * @param sessionValidationScheduler
     * @param simpleCookie
     * @return
     */
    @Bean
    public DefaultWebSessionManager sessionManager(CustomShiroSessionDao customShiroSessionDao,
                                                   CustomShiroSessionListener customShiroSessionListener,
                                                   ExecutorServiceSessionValidationScheduler sessionValidationScheduler,
                                                   SimpleCookie simpleCookie) {

        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //相隔多久检查一次session的有效性
        sessionManager.setSessionValidationInterval(1000 * 60 * 30);//时间为毫秒
        //session 有效时间为半小时
        sessionManager.setGlobalSessionTimeout(1000 * 60 * 30);//时间为毫秒 设置为30分钟

        sessionManager.setSessionDAO(customShiroSessionDao);//使用自定义的session dao
        //设置listeners 可以设置多个
        List<SessionListener> listeners = new ArrayList<>();
        listeners.add(customShiroSessionListener);
        sessionManager.setSessionListeners(listeners);

        sessionManager.setSessionValidationScheduler(sessionValidationScheduler);
        //是否开启 检测，默认开启
        sessionManager.setSessionValidationSchedulerEnabled(true);
        //是否删除无效的，默认也是开启
        sessionManager.setDeleteInvalidSessions(true);
        //会话Cookie模板
        sessionManager.setSessionIdCookie(simpleCookie);

        return sessionManager;
    }

    /**
     * 会话Session ID生成器
     * @return JavaUuidSessionIdGenerator
     */
    @Bean
    public JavaUuidSessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    /**
     * 普通会话Cookie模板
     * @return simpleCookie
     */
    @Bean(name = "simpleCookie")
    public SimpleCookie sessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("s-tk");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(-1);
//        simpleCookie.setDomain("www.xxx.xxxx");//当实现单点登陆时设置该属性
        return simpleCookie;
    }

    /**
     * 记住我会话Cookie模板
     * @return rememberMeCookie
     */
    @Bean(name = "rememberMeCookie")
    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("s-tk");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(60 * 60 * 24 * 30);//以秒为单位
//        simpleCookie.setDomain("www.xxx.xxxx");//当实现单点登陆时设置该属性
        return simpleCookie;
    }

    /**
     * 设置记住我cookie管理器
     * @param rememberMeCookie  cookie
     * @return 管理器CookieRememberMeManager
     */
    @Bean
    public CookieRememberMeManager cookieRememberMeManager(SimpleCookie rememberMeCookie) {
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        rememberMeManager.setCookie(rememberMeCookie);
        rememberMeManager.setCipherKey(org.apache.shiro.codec.Base64.decode("bXktYXBwbGljYXRpb24="));
        return rememberMeManager;
    }

    /**
     * 安全管理器
     * @param mineRealm
     * @param sessionManager
     * @param rememberMeManager
     * @param shiroCacheManager
     * @return
     */
    @Bean
    public DefaultWebSecurityManager securityManager(MineRealm mineRealm,
                                                     DefaultWebSessionManager sessionManager,
                                                     CookieRememberMeManager rememberMeManager,
                                                     CustomShiroCacheManager shiroCacheManager) {

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setRealm(mineRealm);
        securityManager.setSessionManager(sessionManager);
        securityManager.setRememberMeManager(rememberMeManager);
        securityManager.setCacheManager(shiroCacheManager);

        return securityManager;
    }


    @Bean
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager,
                                                         LoginFilter login,
                                                         RoleFilter role,
                                                         PermissionFilter permission,
                                                         AuthFilter auth,
                                                         KickOutFilter kickout,
                                                         ShiroGetChain shiroGetChain) {

        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        factoryBean.setSecurityManager(securityManager);

        Map<String, Filter> filters = new LinkedHashMap<>();
        filters.put("login", login);
        filters.put("role", role);
        filters.put("auth", auth);
        filters.put("permission", permission);
        filters.put("kickout", kickout);
        factoryBean.setFilters(filters);

        factoryBean.setFilterChainDefinitions(shiroGetChain.loadFilterChainDefinitions());

        factoryBean.setLoginUrl("/user/login");
        factoryBean.setSuccessUrl("/");
        factoryBean.setUnauthorizedUrl("/unauth");

        return factoryBean;
    }
    /**
     * 静态注入，相当于调用SecurityUtils.setSecurityManager(securityManager)
     * @param securityManager 注入的manager
     * @return
     */
    @Bean
    public MethodInvokingFactoryBean methodInvoking4Security(DefaultWebSecurityManager securityManager) {
        MethodInvokingFactoryBean method = new MethodInvokingFactoryBean();
        method.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        method.setArguments(new Object[] {securityManager} );
        return method;
    }

    @Bean
    public CustomShiroSessionDao customShiroSessionDao(JavaUuidSessionIdGenerator sessionIdGenerator,
                                                       ShiroSessionDao shiroSessionDao) {
        CustomShiroSessionDao customShiroSessionDao = new CustomShiroSessionDao();
        customShiroSessionDao.setShiroSessionDao(shiroSessionDao);
        customShiroSessionDao.setSessionIdGenerator(sessionIdGenerator);
        return customShiroSessionDao;
    }
}
