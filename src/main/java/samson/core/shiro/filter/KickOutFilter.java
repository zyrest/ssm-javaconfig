package samson.core.shiro.filter;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import samson.common.statics.ResponseStatus;
import samson.common.vo.MessageVo;
import samson.core.shiro.cache.CacheUtil;
import samson.core.shiro.session.ShiroSessionRepository;
import samson.core.shiro.statics.ShiroStatics;
import samson.core.shiro.token.manager.TokenManager;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * Created by 96428 on 2017/7/26.
 * This in ssmjavaconfig, samson.core.shiro.filter
 */
@SuppressWarnings("unchecked")
public class KickOutFilter extends AccessControlFilter {

    //在线用户
    private final String ONLINE_USER =
            KickOutFilter.class.getCanonicalName()+ "_online_user";
    //踢出状态，true标示踢出
    private final String KICKOUT_STATUS =
            KickOutFilter.class.getCanonicalName()+ "_kickout_status";

    @Resource
    private ShiroSessionRepository shiroSessionRepository;

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse,
                                      Object o) throws Exception {

        Subject subject = getSubject(servletRequest, servletResponse);
        Session session = subject.getSession();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uri = request.getRequestURI();
        String basePath = request.getContextPath();
        if (uri != null && uri.startsWith(basePath)) {
            uri = uri.replaceFirst(basePath, "");
        }
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            return Boolean.TRUE;
        }

        Boolean flag = (Boolean) session.getAttribute(KICKOUT_STATUS);
        if (flag != null && flag) {
            if (ShiroFilterUtil.isAjax(servletRequest)) {
                MessageVo message = new MessageVo().setCreateTime(new Date());
                message.setStatus(ResponseStatus.KICK_OUT).setMessage("用户已被踢出，可能存在异地同时登陆状况");
                ShiroFilterUtil.writeJsonToResponse(servletResponse, message);
            }
            return Boolean.FALSE;
        }

        //获取当前sessionId
        Serializable sessionId = session.getId();
        //获取缓存中的在线用户
        LinkedHashMap<Long, Serializable> infoMap
                = CacheUtil.get(ShiroStatics.CACHE_INDEX, ONLINE_USER, LinkedHashMap.class);
        //不存在创建新的
        infoMap = (infoMap == null ? new LinkedHashMap<>() : infoMap );
        //获取当前登陆用户的id
        Long userId = TokenManager.getUserId();
        if (infoMap.containsKey(userId)) {
            Serializable oldSessionId = infoMap.get(userId);

            //同一用户在同一位置登陆 延时
            if (Objects.equals(sessionId, oldSessionId)) {
                CacheUtil.setex(ShiroStatics.CACHE_INDEX, userId, sessionId, 3600);
            } else { //sessionId不相等，说明异处登陆
                Session oldSession = shiroSessionRepository.getSession(oldSessionId);
                if (oldSession == null) { //原session无效
                    shiroSessionRepository.deleteSession(oldSessionId);
                    infoMap.remove(userId);
                    infoMap.put(userId, sessionId);
                    CacheUtil.setex(ShiroStatics.CACHE_INDEX, ONLINE_USER, infoMap, 3600);
                } else { //原session有效，将其踢出
                    oldSession.setAttribute(KICKOUT_STATUS, Boolean.TRUE);
                    shiroSessionRepository.saveSession(oldSession);
                    infoMap.remove(userId);
                    infoMap.put(userId, sessionId);
                    CacheUtil.setex(ShiroStatics.CACHE_INDEX, ONLINE_USER, infoMap, 3600);
                }
            }

        } else {
            infoMap.put(userId, sessionId);
            CacheUtil.setex(ShiroStatics.CACHE_INDEX, ONLINE_USER, infoMap, 3600);
        }


        return Boolean.TRUE;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest,
                                     ServletResponse servletResponse) throws Exception {

        WebUtils.saveRequest(servletRequest);

        //先退出
        Subject subject = getSubject(servletRequest, servletResponse);
        subject.logout();

        //再重定向
        WebUtils.issueRedirect(servletRequest, servletResponse, ShiroFilterUtil.KICKED_OUT);
        return false;
    }

}
