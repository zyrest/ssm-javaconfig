package samson.core.spring;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import samson.common.dao.BaseDao;

import javax.sql.DataSource;

/**
 * Created by 96428 on 2017/7/21.
 * This in ssmjavaconfig, samson.core.spring
 */
@Configuration
//加载资源文件
@PropertySource({"classpath:jdbc.properties"})
//加上这个注解，使得支持事务
@EnableTransactionManagement
@MapperScan(basePackageClasses = {BaseDao.class})
public class MybatisConfig {
    private static final Logger LOGGER = Logger.getLogger(MybatisConfig.class);
    /**
     * 绑定资源属性
     */
    @Value("${jdbc.driver}")
    private String driverClass;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String userName;
    @Value("${jdbc.password}")
    private String passWord;

    @Value("${jdbc.initialSize}")
    private int initialSize;
    @Value("${jdbc.maxActive}")
    private int maxActive;
    @Value("${jdbc.minIdle}")
    private int minIdle;
    @Value("${jdbc.maxWait}")
    private long maxWait;

    @Value("${jdbc.testWhileIdle}")
    private boolean testWhileIdle;
    @Value("${jdbc.timeBetweenEvictionRunsMillis}")
    private long timeBetweenEvictionRunsMillis;
    @Value("${jdbc.validationQuery}")
    private String validationQuery;

    @Value("${jdbc.testOnBorrow}")
    private boolean testOnBorrow;
    @Value("${jdbc.testOnReturn}")
    private boolean testOnReturn;
    @Value("${jdbc.logAbandoned}")
    private boolean logAbandoned;

    @Value("${jdbc.minEvictableIdleTimeMillis}")
    private long minEvictableIdleTimeMillis;
    @Value("${jdbc.removeAbandoned}")
    private boolean removeAbandoned;
    @Value("${jdbc.removeAbandonedTimeout}")
    private int removeAbandonedTimeout;

    /**
     * 自动加载环境变量
     * 必须为static
     * @return PropertySourcesPlaceholderConfigurer
     */
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        LOGGER.info("mybatis PropertySourcesPlaceholderConfigurer");
        return new PropertySourcesPlaceholderConfigurer();
    }


    /**
     * 配置数据源(阿里druid数据池
     * @return BasicDataSource
     */
    @Bean
    public DruidDataSource dataSource() {
        LOGGER.info("load DruidDataSource");
        DruidDataSource dataSource = new DruidDataSource();

        dataSource.setDriverClassName(driverClass); //Druid可以根据url自动推断，可以不用配置
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(passWord);

        dataSource.setInitialSize(initialSize);
        dataSource.setMaxActive(maxActive);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxWait(maxWait);

        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        dataSource.setValidationQuery(validationQuery);

        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestOnReturn(testOnReturn);
        dataSource.setLogAbandoned(logAbandoned);

        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        dataSource.setRemoveAbandoned(removeAbandoned);
        dataSource.setRemoveAbandonedTimeout(removeAbandonedTimeout);

        return dataSource;
    }

    /**
     * 配置sessionFactory，
     * @param dataSource 自动注入
     * @return SqlSessionFactory
     * @throws Exception 异常
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setMapperLocations(resolver.getResources("classpath:Mapper/*Mapper.xml"));

//        //配置pageHelper
//        sqlSessionFactory.setPlugins(new Interceptor[]{pageHelper()});

        return sqlSessionFactory.getObject();
    }

    /**
     * 配置声明式事务管理
     * @param dataSource 数据源
     * @return DataSourceTransactionManager
     */
    @Bean
    public PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

//    /**
//     * mybatis 分页插件配置
//     * @return
//     */
//    @Bean
//    public PageHelper pageHelper() {
//        logger.info("MyBatisConfiguration.pageHelper()");
//        PageHelper pageHelper = new PageHelper();
//        Properties p = new Properties();
//        p.setProperty("offsetAsPageNum", "true");
//        p.setProperty("rowBoundsWithCount", "true");
//        p.setProperty("reasonable", "true");
//        pageHelper.setProperties(p);
//        return pageHelper;
//    }
}
