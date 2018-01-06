package com.zhongyun.smoke.security;

import com.zhongyun.smoke.ApplicationConfig;
import static com.zhongyun.smoke.common.Util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

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
                .passwordEncoder(ENCODER);
//        auth
//                .inMemoryAuthentication()
//                .withUser("alex").password("123456").roles("用户");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().loginPage(config.getLoginPage()).loginProcessingUrl(config.getLoginProcess())
                    .defaultSuccessUrl(config.getLoginSuccess()).failureUrl(config.getLoginFailure()).permitAll()
                .and()
                .authorizeRequests()
//                .regexMatchers(".*").permitAll()
                    .regexMatchers(HttpMethod.POST, "/api/user/.*").hasAuthority(USER_ADMIN)
                    .regexMatchers(HttpMethod.DELETE, "/api/user/.*").hasAuthority(USER_ADMIN)
                    .regexMatchers(HttpMethod.GET, "/api/user/.*").authenticated()
                    .regexMatchers(HttpMethod.PUT, "/api/user/.*").hasAnyAuthority(USER_ADMIN, USER_USER)

                    .regexMatchers(HttpMethod.POST, "/api/project/.*").hasAuthority(USER_ADMIN)
                    .regexMatchers(HttpMethod.DELETE, "/api/project/.*").hasAuthority(USER_ADMIN)
                    .regexMatchers(HttpMethod.PUT, "/api/project/.*").hasAuthority(USER_ADMIN)
                    .regexMatchers(HttpMethod.GET, "/api/project/.*").hasAnyAuthority(USER_ADMIN, USER_USER, USER_OP)

                    .regexMatchers("/api/up/.*").hasAnyAuthority(USER_ADMIN)

                    .regexMatchers("/api/sensor/.*").hasAnyAuthority(USER_ADMIN, USER_USER, USER_OP)

                    .regexMatchers(HttpMethod.POST, "/api/op/.*").hasAnyAuthority(USER_ADMIN, USER_USER, USER_OP)
                    .regexMatchers(HttpMethod.PUT, "/api/op/.*").hasAnyAuthority(USER_ADMIN, USER_USER, USER_OP)
                    .regexMatchers(HttpMethod.DELETE, "/api/op/.*").hasAnyAuthority(USER_ADMIN, USER_USER, USER_OP)
                    .regexMatchers(HttpMethod.GET, "/api/op/.*").hasAnyAuthority(USER_ADMIN, USER_USER, USER_OP)

                    .regexMatchers("/api/report/.*").hasAnyAuthority(USER_ADMIN, USER_OP, USER_USER)

                    .regexMatchers("/api/file/.*").hasAnyAuthority(USER_ADMIN, USER_OP, USER_USER)
                .and()
                .logout().logoutUrl(config.getLogoutProcess()).logoutSuccessUrl(config.getLoginPage())
//                .and()
//                .requiresChannel()
//                    .regexMatchers(".*").requiresSecure()
                .and()
                .rememberMe()
//                    .tokenRepository(new InMemoryTokenRepositoryImpl())
                    .tokenRepository(new JdbcTokenRepositoryImpl())
                    .tokenValiditySeconds(3600 * 24 * 7)
                    .key("zhongyun")
                .and()
                .csrf()
                    .disable();
    }
}
