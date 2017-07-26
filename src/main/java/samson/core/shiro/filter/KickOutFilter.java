package samson.core.shiro.filter;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by 96428 on 2017/7/26.
 * This in ssmjavaconfig, samson.core.shiro.filter
 */
@Component
public class KickOutFilter extends AccessControlFilter {

    //在线用户
    private final static String ONLINE_USER = KickOutFilter.class.getCanonicalName()+ "_online_user";
    //踢出状态，true标示踢出
    private final static String KICKOUT_STATUS = KickOutFilter.class.getCanonicalName()+ "_kickout_status";

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        return false;
    }
}
