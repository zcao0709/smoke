package com.zhongyun.smoke.security;

import com.zhongyun.smoke.ApplicationConfig;
import com.zhongyun.smoke.common.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

import javax.sql.DataSource;

/**
 * Created by caozhennan on 2017/12/12.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private ApplicationConfig config;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("SELECT name, pass, true FROM user WHERE name = ?")
                .authoritiesByUsernameQuery("SELECT name, type FROM user WHERE name = ?")
                .passwordEncoder(Util.ENCODER);
//        auth
//                .inMemoryAuthentication()
//                .withUser("alex").password("123456").roles("用户");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().loginPage(config.getLoginPage()).loginProcessingUrl(config.getLoginProcess())
                    .defaultSuccessUrl(config.getLoginSuccess()).failureUrl(config.getLoginFailure()).permitAll()
                .and()
//                .httpBasic()
//                .and()
                .authorizeRequests()
                    .regexMatchers(HttpMethod.POST, "/api/user/.*").hasAuthority(Util.USER_ADMIN)
                    .regexMatchers(HttpMethod.DELETE, "/api/user/.*").hasAuthority(Util.USER_ADMIN)
//                    .regexMatchers(HttpMethod.GET, "/api/user/.*").hasAnyAuthority(Util.USER_ADMIN, Util.USER_USER, Util.USER_OP)
                    .regexMatchers(HttpMethod.GET, "/api/user/.*").authenticated()
                    .regexMatchers(HttpMethod.PUT, "/api/user/.*").hasAnyAuthority(Util.USER_ADMIN, Util.USER_USER)

                    .regexMatchers(HttpMethod.POST, "/api/project/.*").hasAuthority(Util.USER_ADMIN)
                    .regexMatchers(HttpMethod.DELETE, "/api/project/.*").hasAuthority(Util.USER_ADMIN)
                    .regexMatchers(HttpMethod.PUT, "/api/project/.*").hasAuthority(Util.USER_ADMIN)
                    .regexMatchers(HttpMethod.GET, "/api/project/.*").hasAnyAuthority(Util.USER_ADMIN, Util.USER_USER, Util.USER_OP)

                    .regexMatchers("/api/up/.*").hasAnyAuthority(Util.USER_ADMIN)

                    .regexMatchers("/api/sensor/.*").hasAnyAuthority(Util.USER_ADMIN, Util.USER_USER, Util.USER_OP)

                    .regexMatchers(HttpMethod.POST, "/api/op/.*").hasAnyAuthority(Util.USER_ADMIN, Util.USER_OP)
                    .regexMatchers(HttpMethod.PUT, "/api/op/.*").hasAnyAuthority(Util.USER_ADMIN, Util.USER_OP)
                    .regexMatchers(HttpMethod.DELETE, "/api/op/.*").hasAnyAuthority(Util.USER_ADMIN, Util.USER_OP)
                    .regexMatchers(HttpMethod.GET, "/api/op/.*").hasAuthority(Util.USER_USER)

                    .regexMatchers("/api/report/.*").hasAnyAuthority(Util.USER_ADMIN, Util.USER_OP, Util.USER_USER)
                .and()
                .logout().logoutUrl(config.getLogoutProcess()).logoutSuccessUrl(config.getLoginPage())
//                .and()
//                .requiresChannel()
//                    .regexMatchers(".*").requiresSecure()
                .and()
                .rememberMe()
                    .tokenRepository(new InMemoryTokenRepositoryImpl())
                    .tokenValiditySeconds(3600 * 24)
                    .key("zhongyun")
                .and()
                .csrf()
                    .disable();
    }
}
