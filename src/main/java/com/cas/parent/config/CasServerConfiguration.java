package com.cas.parent.config;

import com.cas.parent.config.properties.CasProperties;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.servlet.http.HttpSessionEvent;

/**
 * Created by khan on 2/28/18.
 */
@Configuration
public class CasServerConfiguration {

  @Autowired
  private CasProperties casProperties;

  @Bean
  public ServiceProperties serviceProperties() {
    ServiceProperties serviceProperties = new ServiceProperties();
    serviceProperties.setService(casProperties.getServiceHost() + "/login/cas");
    serviceProperties.setSendRenew(false);
    return serviceProperties;
  }

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint(ServiceProperties serviceProperties) {
    CasAuthenticationEntryPoint entryPoint = new CasAuthenticationEntryPoint();
    entryPoint.setLoginUrl(casProperties.getServerHost() + casProperties.getLoginUri());
    entryPoint.setServiceProperties(serviceProperties);
    return entryPoint;
  }

  @Bean
  public TicketValidator ticketValidator() {
    return new Cas30ServiceTicketValidator(casProperties.getServerHost());
  }

  @Bean
  public CasAuthenticationProvider casAuthenticationProvider () {
    CasAuthenticationProvider authenticationProvider = new CasAuthenticationProvider();
    authenticationProvider.setServiceProperties(serviceProperties());
    authenticationProvider.setTicketValidator(ticketValidator());
    authenticationProvider.setUserDetailsService((s) -> new User("user-dev-cc","user-dev-cc",
        true,true,true,true,
        AuthorityUtils.createAuthorityList("ROLE_ADMIN")));
    authenticationProvider.setKey("CAS-AUTHENTICATION_SERVER");
    return authenticationProvider;
  }

  @Bean
  public SecurityContextLogoutHandler securityContextLogoutHandler() {
    return new SecurityContextLogoutHandler();
  }

  @Bean
  public LogoutFilter logoutFilter() {
    LogoutFilter logoutFilter = new LogoutFilter(
        casProperties.getServerHost() + casProperties.getLogoutUri() + "?service="
            + casProperties.getServiceHost(), securityContextLogoutHandler());
    logoutFilter.setFilterProcessesUrl("/logout/cas");
    return logoutFilter;
  }

  @Bean
  public SingleSignOutFilter singleSignOutFilter() {
    SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
    singleSignOutFilter.setCasServerUrlPrefix(casProperties.getServerHost());
    singleSignOutFilter.setIgnoreInitConfiguration(true);
    return singleSignOutFilter;
  }

  @EventListener
  public SingleSignOutHttpSessionListener singleSignOutHttpSessionListener(
      HttpSessionEvent event) {
    return new SingleSignOutHttpSessionListener();
  }
}
