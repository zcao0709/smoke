package com.zhongyun.smoke;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.servlet.MultipartConfigElement;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * Created by caozhennan on 2017/11/16.
 */
@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationConfig {

    @Value("${aserver.ip}")
    private String aserverIp;

    @Value("${aserver.port}")
    private int aserverPort;

    @Value("${aserver.user}")
    private String aserverUser;

    @Value("${aserver.pwd}")
    private String aserverPwd;

    @Value("${mongo.ip}")
    private String mongoIp;

    @Value("${mongo.port}")
    private int mongoPort;

    @Value("${siter.port}")
    private int siterPort;

    @Value("${mysql.url}")
    private String mysqlUrl;

    @Value("${mysql.user}")
    private String mysqlUser;

    @Value("${mysql.pass}")
    private String mysqlPass;

    @Value("${login.page}")
    private String loginPage;

    @Value("${login.process}")
    private String loginProcess;

    @Value("${login.success}")
    private String loginSuccess;

    @Value("${login.failure}")
    private String loginFailure;

    @Value("${logout.process}")
    private String logoutProcess;

    @Value("${sms.id}")
    private String smsId;

    @Value("${sms.sec}")
    private String smsSec;

    @Value("${file.location}")
    private String fileLocation;

    @Value("${gateway.timeout}")
    private long gatewayTimeout;

    @Value("${sensor.timeout}")
    private long sensorTimeout;

    @Value("${admin.phone}")
    private String adminPhone;

    public long getGatewayTimeout() {
        return gatewayTimeout;
    }

    public long getSensorTimeout() {
        return sensorTimeout;
    }

    public String getAdminPhone() {
        return adminPhone;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public String getSmsId() {
        return smsId;
    }

    public String getSmsSec() {
        return smsSec;
    }

    public String getLoginPage() {
        return loginPage;
    }

    public String getLoginProcess() {
        return loginProcess;
    }

    public String getLoginSuccess() {
        return loginSuccess;
    }

    public String getLoginFailure() {
        return loginFailure;
    }

    public String getLogoutProcess() {
        return logoutProcess;
    }

    public String getAserverIp() {
        return aserverIp;
    }

    public int getAserverPort() {
        return aserverPort;
    }

    public String getAserverUser() {
        return aserverUser;
    }

    public String getAserverPwd() {
        return aserverPwd;
    }

    public int getSiterPort() {
        return siterPort;
    }

    @Bean
    public Mongo mongo() throws Exception {
        return new MongoClient(mongoIp, mongoPort);
    }

    @Primary
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl(mysqlUrl);
        ds.setUsername(mysqlUser);
        ds.setPassword(mysqlPass);
        return ds;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        return new MultipartConfigElement("/tmp/", 4194304, 4194304, 0);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
