package com.cas.parent.config;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders
    .AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration
    .WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import java.util.Arrays;

/**
 * Created by khan on 3/1/18.
 */
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private AuthenticationEntryPoint entryPoint;

  @Autowired
  private SingleSignOutFilter singleSignOutFilter;
  @Autowired
  private LogoutFilter logoutFilter;

  @Autowired
  private AuthenticationProvider authenticationProvider;

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.authorizeRequests().regexMatchers( "/secured.*", "/login").authenticated()
        .and().httpBasic().authenticationEntryPoint(entryPoint).and().logout()
        .logoutSuccessUrl("/logout").and()
        .addFilterBefore(singleSignOutFilter, CasAuthenticationFilter.class)
        .addFilterBefore(logoutFilter, LogoutFilter.class).csrf().disable();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder managerBuilder) throws Exception {
    managerBuilder.authenticationProvider(authenticationProvider);
  }

  @Override
  protected AuthenticationManager authenticationManager() throws Exception {
    return new ProviderManager(Arrays.asList(authenticationProvider));
  }

  @Bean
  public CasAuthenticationFilter casAuthenticationFilter(ServiceProperties serviceProperties)
      throws Exception {
    CasAuthenticationFilter authenticationFilter = new CasAuthenticationFilter();
    authenticationFilter.setServiceProperties(serviceProperties);
    authenticationFilter.setAuthenticationManager(authenticationManager());
    return authenticationFilter;
  }
}
