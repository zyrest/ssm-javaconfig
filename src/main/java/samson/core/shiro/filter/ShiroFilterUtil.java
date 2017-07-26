package samson.core.shiro.filter;

import com.alibaba.fastjson.JSON;
import samson.common.util.LoggerUtil;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by 96428 on 2017/7/25.
 * This in ssmjavaconfig, samson.core.shiro.filter
 */
public class ShiroFilterUtil {
    public static final Class<? extends ShiroFilterUtil> CLAZZ = ShiroFilterUtil.class;
    //登录页面
    public static final String LOGIN_URL = "";//TODO
    //踢出登录提示
    public static final String KICKED_OUT = "";//TODO
    //没有权限提醒
    public static final String UNAUTHORIZED = "";//TODO

    /**
     * 判断请求是不是ajax发出
     * @param request
     * @return
     */
    public static boolean isAjax(ServletRequest request) {

        return ((HttpServletRequest) request).getHeader("X-Requested-With")
                .equalsIgnoreCase("XMLHttpRequest");
    }

    public static void writeJsonToResponse(ServletResponse response, Map<Object, Object> map) {
        if (map == null || map.size() == 0) return;

        response.setCharacterEncoding("UTF-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.write(JSON.toJSONString(map));
        } catch (IOException e) {
            LoggerUtil.fmtError(CLAZZ, e, "获取response的writer时出错");
        } finally {
            if (pw != null) {
                pw.flush();
                pw.close();
            }
        }

    }
}
