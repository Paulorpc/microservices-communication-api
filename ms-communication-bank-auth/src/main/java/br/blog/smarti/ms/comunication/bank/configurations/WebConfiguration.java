package br.blog.smarti.ms.comunication.bank.configurations;

import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@EnableAutoConfiguration
@KeycloakConfiguration
public class WebConfiguration extends WebMvcConfigurationSupport {

  @Override
  protected void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**").allowedOrigins("*").allowedMethods("POST,PUT,GET,DELETE,OPTIONS")
        .allowedHeaders("Location", "Content-type", "authorization", "x-sign-certificate")
        .exposedHeaders("Location", "Content-Disposition");
  }

  @Bean
  @Primary
  @Scope(scopeName = SCOPE_REQUEST, proxyMode = TARGET_CLASS)
  @Profile("!test")
  public KeycloakSecurityContext securityContext() {
    RefreshableKeycloakSecurityContext context =
        (RefreshableKeycloakSecurityContext) RequestContextHolder.currentRequestAttributes()
            .getAttribute(KeycloakSecurityContext.class.getName(), RequestAttributes.SCOPE_REQUEST);
    return context;
  }
}
