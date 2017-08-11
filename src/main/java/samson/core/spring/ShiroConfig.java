package samson.core.spring;

import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import samson.core.shiro.CustomShiroSessionDao;
import samson.core.shiro.cache.JedisManager;
import samson.core.shiro.cache.JedisShiroSessionRepository;
import samson.core.shiro.cache.impl.CustomShiroCacheManager;
import samson.core.shiro.cache.impl.JedisShiroCacheManager;
import samson.core.shiro.filter.*;
import samson.core.shiro.listener.CustomShiroSessionListener;
import samson.core.shiro.service.LoadFiltersChainsManager;
import samson.core.shiro.service.impl.LoadFiltersChainsManagerImpl;
import samson.core.shiro.session.CustomSessionManager;
import samson.core.shiro.token.CustomCredentialsMatcher;
import samson.core.shiro.token.MineRealm;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by 96428 on 2017/7/21.
 * This in ssmjavaconfig, samson.core.spring
 */
@Configuration
public class ShiroConfig {

    @Bean
    public JedisShiroCacheManager jedisShiroCacheManager() {

//        jedisShiroCacheManager.setJedisManager( jedisManager() );

        return new JedisShiroCacheManager();
    }

    @Bean
    public JedisManager jedisManager() {
        return new JedisManager();
    }

    /**
     * 静态注入，相当于调用SecurityUtils.setSecurityManager(securityManager)
     * @return
     */

    @Bean
    public JedisShiroSessionRepository jedisShiroSessionRepository() {

//        sessionRepository.setJedisManager( jedisManager() );

        return new JedisShiroSessionRepository();
    }

    @Bean
    public CustomShiroCacheManager customShiroCacheManager() {
        return new CustomShiroCacheManager();
    }

    /**
     * 会话Session ID生成器
     * @return JavaUuidSessionIdGenerator
     */
    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }


    @Bean
    public CustomShiroSessionDao customShiroSessionDao() {
        CustomShiroSessionDao shiroSessionDao = new CustomShiroSessionDao();

        shiroSessionDao.setSessionIdGenerator( sessionIdGenerator() );

        return shiroSessionDao;
    }

    @Bean
    public LoadFiltersChainsManager loadFiltersChainsManager() {
        return new LoadFiltersChainsManagerImpl();
    }
//    @Bean
//    public ShiroManagerImpl shiroManager() {
//        return new ShiroManagerImpl();
//    }
    //filters start
    @Bean
    public LoginFilter login() {
        return new LoginFilter();
    }
    @Bean
    public RoleFilter role() {
        return new RoleFilter();
    }
    @Bean
    public AuthFilter auth() {
        return new AuthFilter();
    }
    @Bean
    public PermissionFilter permission() {
        return new PermissionFilter();
    }
    @Bean
    public KickOutFilter kickout() {
        return new KickOutFilter();
    }
    //filters end
    @Bean
    public ShiroFilterFactoryBean shiroFilter() {

        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        factoryBean.setSecurityManager( securityManager() );

        Map<String, Filter> filters = new LinkedHashMap<>();
        filters.put("login", login());
        filters.put("role", role());
        filters.put("auth", auth());
        filters.put("permission", permission());
        filters.put("kickout", kickout());
        factoryBean.setFilters(filters);

        factoryBean.setFilterChainDefinitions( loadFiltersChainsManager().load() );

        factoryBean.setLoginUrl("/u/login");
        factoryBean.setSuccessUrl("/");
        factoryBean.setUnauthorizedUrl("/unauth");

        return factoryBean;
    }


    @Bean
    public CustomCredentialsMatcher customCredentialsMatcher() {
        return new CustomCredentialsMatcher();
    }
    /**
     * 使用自定义的凭证匹配器
     * @return MineRealm
     */
    @Bean
    public MineRealm mineRealm() {
        MineRealm mineRealm = new MineRealm();

//        mineRealm.setCacheManager( customShiroCacheManager() );
        mineRealm.setCredentialsMatcher( customCredentialsMatcher() );

        return mineRealm;
    }


    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {

        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();

        creator.setProxyTargetClass(true);

        return creator;
    }
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {

        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();

        advisor.setSecurityManager( securityManager() );

        return advisor;
    }

    /**
     * 普通会话Cookie模板
     * @return simpleCookie
     */
    @Bean("sessionIdCookie")
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
     * @return 管理器CookieRememberMeManager
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();

        rememberMeManager.setCookie( rememberMeCookie() );
        rememberMeManager.setCipherKey(org.apache.shiro.codec.Base64.decode("bXktYXBwbGljYXRpb24="));

        return rememberMeManager;
    }

    @Bean
    public CustomSessionManager customSessionManager() {

//        sessionManager.setShiroSessionRepository( jedisShiroSessionRepository() );
//        sessionManager.setCustomShiroSessionDao( customShiroSessionDao() );

        return new CustomSessionManager();
    }


    /**
     * Session Manager
     * @return
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {

        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
//        //相隔多久检查一次session的有效性
//        sessionManager.setSessionValidationInterval(1000 * 60 * 30);//时间为毫秒
//        //session 有效时间为半小时
//        sessionManager.setGlobalSessionTimeout(1000 * 60 * 30);//时间为毫秒 设置为30分钟

        //设置listeners 可以设置多个
//        List<SessionListener> listeners = new ArrayList<>();
//        listeners.add( customSessionListener() );
//        sessionManager.setSessionListeners(listeners);
//
//        sessionManager.setSessionValidationScheduler(sessionValidationScheduler());
//        //是否开启 检测，默认开启
//        sessionManager.setSessionValidationSchedulerEnabled(true);
//        //是否删除无效的，默认也是开启
//        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionDAO( customShiroSessionDao() );//使用自定义的session dao
        //会话Cookie模板
        sessionManager.setSessionIdCookie( sessionIdCookie() );
        sessionManager.setSessionIdCookieEnabled(true);

        return sessionManager;
    }

    @Bean
    public DefaultWebSecurityManager securityManager() {

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setRealm( mineRealm() );
        securityManager.setSessionManager( sessionManager() );
        securityManager.setRememberMeManager( rememberMeManager() );
        securityManager.setCacheManager( customShiroCacheManager() );

        return securityManager;
    }

    @Bean
    public MethodInvokingFactoryBean methodInvoking4Security() {

        MethodInvokingFactoryBean method = new MethodInvokingFactoryBean();

        method.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        method.setArguments(new Object[] { securityManager() } );

        return method;
    }

    @Bean
    public CustomShiroSessionListener customSessionListener() {

//        sessionListener.setShiroSessionRepository(jedisShiroSessionRepository());

        return new CustomShiroSessionListener();
    }

    /**
     * 会话验证调度器
     * @return ExecutorServiceSessionValidationScheduler
     */
    @Bean
    public ExecutorServiceSessionValidationScheduler sessionValidationScheduler() {

        ExecutorServiceSessionValidationScheduler sessionValidationScheduler = new ExecutorServiceSessionValidationScheduler();

        //间隔多少时间检查，不配置是60分钟
        sessionValidationScheduler.setInterval(1000 * 60 * 60 * 5);
//        sessionValidationScheduler.setSessionManager( sessionManager );

        return sessionValidationScheduler;
    }









}
