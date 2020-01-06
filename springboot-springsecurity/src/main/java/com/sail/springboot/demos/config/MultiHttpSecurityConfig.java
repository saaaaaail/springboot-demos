package com.sail.springboot.demos.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: springboot-demos
 * @description:
 * @author: yangfan
 * @create: 2020/01/02 09:17
 */


@EnableWebSecurity
public class MultiHttpSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("password").roles("USER").and()
                .withUser("manager").password("password").roles("MANAGER");
    }

    @Configuration
    @Order(1)
    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter{
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/web/**")
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/web/report/**").hasRole("MANAGER")
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    //登录页面的接口
                    .loginPage("/web/login")
                    //
                    //.loginProcessingUrl("/process")
                    //自定义用户名
                    //.usernameParameter("user")
                    //自定义密码
                    //.passwordParameter("password")
                    //登录失败转发到下面的url
                    //.failureForwardUrl("")
                    //登录成功转发到下面的url
                    //.defaultSuccessUrl("")
                    //.successForwardUrl("")
                    //自定义认证成功的处理器
                    //.successHandler((httpServletRequest, httpServletResponse, authentication) -> { })
                    //form表单功能开放
                    .permitAll();
        }
    }

    @Configuration
    @Order(2)
    public static class RestSecurityConfig extends WebSecurityConfigurerAdapter{
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/rest/**")
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/rest/hello/**").hasRole("USER")
                    .anyRequest().authenticated()
                    .and()
                    .httpBasic();
        }
    }
}
