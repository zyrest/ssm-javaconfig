package samson.core.shiro.filter;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Component;
import samson.common.statics.ResponseStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 96428 on 2017/7/25.
 * This in ssmjavaconfig, samson.core.shiro.filter
 */
@Component
public class PermissionFilter extends AccessControlFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse,
                                      Object o) throws Exception {

        Subject subject = getSubject(servletRequest, servletResponse);
        if (o != null) {
            String[] permissions = (String[]) o;
            for (String permission : permissions) {
                if (subject.isPermitted(permission)) return Boolean.TRUE;
            }
        }

        /**
         * 此处是改版后，为了兼容项目不需要部署到root下，也可以正常运行，但是权限没设置目前必须到root 的URI，
         * 原因：如果你把这个项目叫 ShiroDemo，那么路径就是 /ShiroDemo/xxxx.shtml ，
         * 那另外一个人使用，又叫Shiro_Demo,那么就要这么控制/Shiro_Demo/xxxx.shtml
         * 理解了吗？
         * 所以这里替换了一下，使用根目录开始的URI
         */
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uri = request.getRequestURI();
        String basePath = request.getContextPath();
        if(null != uri && uri.startsWith(basePath)){
            uri = uri.replace(basePath, "");
        }
        if (subject.isPermitted(uri)) {
            return Boolean.TRUE;
        }
        if (ShiroFilterUtil.isAjax(servletRequest)) {
            Map<Object, Object> map = new HashMap<>();
            map.put("status", ResponseStatus.NO_PERMISSION);
            map.put("message", "用户没有权限");
            ShiroFilterUtil.writeJsonToResponse(servletResponse, map);
        }
        return Boolean.FALSE;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest,
                                     ServletResponse servletResponse) throws Exception {

        Subject subject = getSubject(servletRequest, servletResponse);
        if (subject.getPrincipal() == null) {
            WebUtils.saveRequest(servletRequest);
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
