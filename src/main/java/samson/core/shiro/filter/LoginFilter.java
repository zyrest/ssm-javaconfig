package samson.core.shiro.filter;


import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import samson.common.po.User;
import samson.common.statics.ResponseStatus;
import samson.core.shiro.token.manager.TokenManager;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 96428 on 2017/7/25.
 * This in ssmjavaconfig, samson.core.shiro.filter
 */
public class LoginFilter extends AccessControlFilter{
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse,
                                      Object o) throws Exception {

        User token = TokenManager.getToken();

        if (token != null || isLoginRequest(servletRequest, servletResponse)) {
            return Boolean.TRUE;
        }

        if (ShiroFilterUtil.isAjax(servletRequest)) {
            Map<Object, Object> map = new HashMap<>();
            map.put("status", ResponseStatus.NO_PERMISSION);
            map.put("message", "用户未登录");
            ShiroFilterUtil.writeJsonToResponse(servletResponse, map);
        }
        return Boolean.FALSE;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest,
                                     ServletResponse servletResponse) throws Exception {

        //保存Request和Response 到登录后的链接
        saveRequest(servletRequest);
        WebUtils.issueRedirect(servletRequest, servletResponse, ShiroFilterUtil.LOGIN_URL);
        return false;
    }
}
