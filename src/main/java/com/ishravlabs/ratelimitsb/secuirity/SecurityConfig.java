package com.ishravlabs.ratelimitsb.secuirity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
        .withUser("foo")
        .password("foo")
        .roles("USER")
        .and()
        .withUser("fee")
        .password("fee")
        .roles("ADMIN");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/admin").hasAnyRole("ADMIN")
        .antMatchers("/user").hasAnyRole("USER", "ADMIN")
        .antMatchers("/**").permitAll()
        .and()
        .csrf()
        .disable()
//            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            .and()
        .formLogin();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    // This is a simple password encoder for demonstration purposes. In a production application,
    // use a secure encoder.
    return NoOpPasswordEncoder.getInstance();
  }
}
