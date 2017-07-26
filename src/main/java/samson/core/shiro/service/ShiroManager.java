package samson.core.shiro.service;

/**
 * Created by 96428 on 2017/7/25.
 * This in ssmjavaconfig, samson.core.shiro.service
 */
public interface ShiroManager {

    /**
     * 重新构建权限过滤器
     * 一般在修改了用户角色、用户等信息时，需要再次调用该方法
     */
    void reCreateFilterChains();
}
