package com.example.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests().antMatchers("/h2-console/**").permitAll()
                .and()
                .headers().frameOptions().disable()
                .and()
                .csrf().disable();
        super.configure(httpSecurity);
    }
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                // enable in memory based authentication with a user named "user" and "admin"
//                .inMemoryAuthentication().withUser("user").password("password").roles("USER")
//                .and().withUser("admin").password("password").roles("USER", "ADMIN");
//    }
}

