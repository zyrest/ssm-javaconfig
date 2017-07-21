package samson.core.web;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import samson.core.spring.BaseConfig;
import samson.core.spring.MvcConfig;

/**
 * Created by 96428 on 2017/7/21.
 * This in ssmjavaconfig, samson.core.web
 */
public class DispatcherServletConfigInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     *  加载驱动应用后端的中间层和数据层组件
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{ BaseConfig.class };
    }

    /** 指定配置类
     *  加载包含web组件的bean,如控制机器、视图解析器以及映射处理器
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{ MvcConfig.class };
    }

    /**
     * 将DispatcherServlet 映射到“*.do”
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{ "/" };
    }
}
