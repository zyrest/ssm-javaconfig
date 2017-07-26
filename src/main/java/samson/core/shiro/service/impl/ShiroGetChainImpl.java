package samson.core.shiro.service.impl;

import org.springframework.stereotype.Component;
import samson.common.util.ClassPathUtil;
import samson.common.util.LoggerUtil;
import samson.core.shiro.config.INI;
import samson.core.shiro.service.ShiroGetChain;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * Created by 96428 on 2017/7/26.
 * This in ssmjavaconfig, samson.core.shiro.service.impl
 */
@Component
public class ShiroGetChainImpl implements ShiroGetChain {
    private static final String LF = "\n";
//
//    @Resource
//    private ShiroFilterFactoryBean filterFactoryBean;

    private String getFileAuthRule() {
        String fileName = "shiro_base_auth.ini";
        File file = ClassPathUtil.getResourceFile(fileName);

        StringBuilder builder = new StringBuilder("");

        INI ini = null;
        try {
            ini = new INI(file);
        } catch (FileNotFoundException e) {
            LoggerUtil.fmtDebug(getClass(), "获取文件%s时出错，请检查文件是否存在", fileName);
        }

        if (ini == null) {
            LoggerUtil.fmtDebug(getClass(), "获取文件%s时出错，请检查文件是否存在", fileName);
            return null;
        }

        Map<String, String> rules = ini.get("base_auth");
        if (rules != null && rules.size() != 0) {
            for (String key : rules.keySet()) {
                builder.append(key).append(" = ").append(rules.get(key)).append(LF);
            }
        }
        return builder.toString();
    }

    @Override
    public String loadFilterChainDefinitions() {

        return getFileAuthRule();
    }
}
