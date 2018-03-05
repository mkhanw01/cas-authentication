package com.cas.parent.config.properties;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by khan on 2/28/18.
 */
@Data
@ConfigurationProperties(prefix = "cas.parameter")
@Configuration
public class CasProperties {

  private String serverHost;
  private String serviceHost;
  private String loginUri;
  private String logoutUri;
}
