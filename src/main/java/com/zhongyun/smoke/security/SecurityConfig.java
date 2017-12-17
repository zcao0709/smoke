package com.zhongyun.smoke.security;

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
//        http.formLogin().loginPage("/login.html").successForwardUrl("http://localhost:8191/api/user/v1").failureForwardUrl("/login.html?error=用户名或密码错误")
        http.formLogin().loginPage("/login.html").defaultSuccessUrl("/html/index.html").failureUrl("/login.html?error=用户名或密码错误")
                    .loginProcessingUrl("/login")
                .and()
//                .httpBasic()
//                .and()
                .authorizeRequests()
                    .regexMatchers(HttpMethod.POST, "/html/.*").authenticated()

                    .regexMatchers(HttpMethod.POST, "/api/user/.*").hasAuthority(Util.USER_ADMIN)
                    .regexMatchers(HttpMethod.DELETE, "/api/user/.*").hasAuthority(Util.USER_ADMIN)
                    .regexMatchers(HttpMethod.GET, "/api/user/.*").hasAuthority(Util.USER_ADMIN)
                    .regexMatchers(HttpMethod.PUT, "/api/user/.*").hasAnyAuthority(Util.USER_ADMIN, Util.USER_USER)

                    .regexMatchers(HttpMethod.POST, "/api/project/.*").hasAuthority(Util.USER_ADMIN)
                    .regexMatchers(HttpMethod.DELETE, "/api/project/.*").hasAuthority(Util.USER_ADMIN)
                    .regexMatchers(HttpMethod.PUT, "/api/project/.*").hasAuthority(Util.USER_ADMIN)
                    .regexMatchers(HttpMethod.GET, "/api/project/.*").hasAnyAuthority(Util.USER_ADMIN, Util.USER_USER, Util.USER_OP)

                    .regexMatchers("/api/up/.*").hasAnyAuthority(Util.USER_ADMIN)

                    .regexMatchers("/api/sensor/.*").hasAnyAuthority(Util.USER_ADMIN, Util.USER_USER, Util.USER_OP)

                    .regexMatchers("/api/op/.*").hasAnyAuthority(Util.USER_ADMIN, Util.USER_OP)

                    .regexMatchers("/api/report/.*").hasAnyAuthority(Util.USER_ADMIN, Util.USER_OP, Util.USER_USER)
                .and()
                .logout().logoutSuccessUrl("/login.html")
//                .and()
//                .requiresChannel()
//                    .regexMatchers(".*").requiresSecure()
                .and()
                .rememberMe()
                    .tokenRepository(new InMemoryTokenRepositoryImpl())
                    .tokenValiditySeconds(2419200)
                    .key("zhongyun")
                .and()
                .csrf()
                    .disable();
    }
}
