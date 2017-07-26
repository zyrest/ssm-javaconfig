package samson.core.shiro.filter;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Component;
import samson.common.statics.ResponseStatus;
import samson.common.util.LoggerUtil;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 96428 on 2017/7/25.
 * This in ssmjavaconfig, samson.core.shiro.filter
 */
@Component
public class RoleFilter extends AccessControlFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse,
                                      Object o) throws Exception {

        Subject subject = getSubject(servletRequest, servletResponse);
        if (o != null) {
            String[] roles = (String[]) o;
            for (String role : roles) {
                if (subject.hasRole("role:" + role)) return Boolean.TRUE;
            }
        }

        if (ShiroFilterUtil.isAjax(servletRequest)) {
            Map<Object, Object> map = new HashMap<>();
            LoggerUtil.fmtDebug(getClass(), "%s没有role", subject.getPrincipal().toString());
            map.put("status", ResponseStatus.NO_PERMISSION);
            map.put("message", "用户没有角色");
            ShiroFilterUtil.writeJsonToResponse(servletResponse, map);
        }
        return Boolean.FALSE;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest,
                                     ServletResponse servletResponse) throws Exception {

        Subject subject = getSubject(servletRequest, servletResponse);

        if (subject.getPrincipal() == null) {
            saveRequest(servletRequest);
            WebUtils.issueRedirect(servletRequest, servletResponse, ShiroFilterUtil.LOGIN_URL);
        } else {
            if (StringUtils.isNotBlank(ShiroFilterUtil.UNAUTHORIZED)) { //指定未授权页面
                WebUtils.issueRedirect(servletRequest, servletResponse, ShiroFilterUtil.UNAUTHORIZED);
            } else { //未指定页面 返回401未授权错误码
                WebUtils.toHttp(servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }

        return Boolean.FALSE;
    }
}
