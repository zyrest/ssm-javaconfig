package samson.core.shiro.filter;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import samson.common.statics.ResponseStatus;
import samson.core.shiro.session.SessionStatus;
import samson.core.shiro.statics.ShiroStatics;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 96428 on 2017/7/25.
 * This in ssmjavaconfig, samson.core.shiro.filter
 */
public class AuthFilter extends AccessControlFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse,
                                      Object o) throws Exception {

//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//
//        String uri = request.getRequestURI();
//        if (uri.startsWith(""))

        Subject subject = getSubject(servletRequest, servletResponse);
        Session session = subject.getSession();
        SessionStatus status = (SessionStatus) session.getAttribute(ShiroStatics.SESSION_STATUS);

        if (status != null && !status.isOnlineStatus()) {
            if (ShiroFilterUtil.isAjax(servletRequest)) {
                Map<Object, Object> map = new HashMap<>();
                map.put("status", ResponseStatus.NO_PERMISSION);
                map.put("message", "用户已经被踢出");
                ShiroFilterUtil.writeJsonToResponse(servletResponse, map);
            }
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest,
                                     ServletResponse servletResponse) throws Exception {

        //先退出
        Subject subject = getSubject(servletRequest, servletResponse);
        subject.logout();
        /**
         * 保存Request，用来保存当前Request，然后登录后可以跳转到当前浏览的页面。
         * 比如：
         * 我要访问一个URL地址，/admin/index.html，这个页面是要登录。然后要跳转到登录页面，
         * 但是登录后要跳转回来到/admin/index.html这个地址，怎么办？
         * 传统的解决方法是变成/user/login.shtml?redirectUrl=/admin/index.html。
         * shiro的解决办法不是这样的。
         * 需要：<code>WebUtils.getSavedRequest(request);</code>
         * 然后：{@link UserLoginController.submitLogin(...)}中的
         * <code>String url = WebUtils.getSavedRequest(request).getRequestUrl();</code>
         * 如果还有问题，请咨询我。
         */
        WebUtils.saveRequest(servletRequest);
        //再重定向
        WebUtils.issueRedirect(servletRequest, servletResponse, ShiroFilterUtil.KICKED_OUT);
        return false;
    }
}
