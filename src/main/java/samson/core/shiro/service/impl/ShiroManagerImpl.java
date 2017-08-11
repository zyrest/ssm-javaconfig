package samson.core.shiro.service.impl;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import samson.common.util.LoggerUtil;
import samson.core.shiro.service.LoadFiltersChainsManager;
import samson.core.shiro.service.ShiroManager;

import java.util.Map;

/**
 * Created by 96428 on 2017/7/25.
 * This in ssmjavaconfig, samson.core.shiro.service.impl
 */
public class ShiroManagerImpl implements ShiroManager {

//    @Resource
    private ShiroFilterFactoryBean shiroFilter;

//    @Resource
//    @Autowired
    private LoadFiltersChainsManager loadFiltersChainsManager;

    @Override
    public synchronized void reCreateFilterChains() {
        AbstractShiroFilter filter = null;
        try {
            filter = (AbstractShiroFilter) shiroFilter.getObject();
        } catch (Exception e) {
            LoggerUtil.fmtError(getClass(), e, "在filterFactoryBean中获取filter失败， 请检查配置");
            throw new RuntimeException("获取filter失败");
        }

        PathMatchingFilterChainResolver filterChainResolver =
                (PathMatchingFilterChainResolver) filter.getFilterChainResolver();

        DefaultFilterChainManager chainManager =
                (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();

        //清除老的权限
        chainManager.getFilterChains().clear();
        shiroFilter.getFilterChainDefinitionMap().clear();
        shiroFilter.setFilterChainDefinitions(loadFiltersChainsManager.load());

        //重新构建生成
        Map<String, String> chains = shiroFilter.getFilterChainDefinitionMap();
        for (Map.Entry<String, String> entry : chains.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().trim().replace(" ", "");
            chainManager.createChain(key, value);
        }
    }
}
