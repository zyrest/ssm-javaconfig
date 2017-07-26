package samson.core.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import samson.ConfigMark;

/**
 * Created by 96428 on 2017/7/21.
 * This in ssmjavaconfig, samson.core.spring
 */
@Configuration
@ComponentScan(basePackageClasses = ConfigMark.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class)}
        )
public class BaseConfig { }
