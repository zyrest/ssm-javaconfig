package samson.core.spring;

import org.springframework.beans.factory.config.PlaceholderConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import samson.ConfigMark;

/**
 * Created by 96428 on 2017/7/21.
 * This in ssmjavaconfig, samson.core.spring
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = {ConfigMark.class},
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class)}
        )
public class MvcConfig extends WebMvcConfigurerAdapter {

    // 配置JSP视图解析器
    // 名为home的视图被解析为classpath:controller/home.jsp
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
         resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        resolver.setExposeContextBeansAsAttributes(true);
        return resolver;
    }

    // 自动加载环境变量
    @Bean
    public static PlaceholderConfigurerSupport placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    // 配置文件上传
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
//    @Bean
//    public CommonsMultipartResolver commonsMultipartResolver() {
//        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
//        multipartResolver.setDefaultEncoding("UTF-8");
//        multipartResolver.setMaxUploadSize(2097152);
//        multipartResolver.setMaxInMemorySize(0);
//        return multipartResolver ;
//    }

    //配置静态资源的处理
    //使DispatcherServlet对静态资源的请求转发到Servlet容器默认的Servlet上，
    //而不是使用DispatcherServlet本身来处理此类请求
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer)
    {
        configurer.enable();
    }

}
