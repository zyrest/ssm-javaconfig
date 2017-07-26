package samson.core.shiro.service.impl;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import samson.common.util.LoggerUtil;
import samson.core.shiro.service.ShiroGetChain;
import samson.core.shiro.service.ShiroManager;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by 96428 on 2017/7/25.
 * This in ssmjavaconfig, samson.core.shiro.service.impl
 */
@Component
public class ShiroManagerImpl implements ShiroManager {

    @Autowired
    private ShiroFilterFactoryBean shiroFilter;

    @Autowired
    private ShiroGetChain shiroGetChain;


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
        shiroFilter.setFilterChainDefinitions(shiroGetChain.loadFilterChainDefinitions());

        //重新构建生成
        Map<String, String> chains = shiroFilter.getFilterChainDefinitionMap();
        for (Map.Entry<String, String> entry : chains.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().trim().replace(" ", "");
            chainManager.createChain(key, value);
        }
    }
}
