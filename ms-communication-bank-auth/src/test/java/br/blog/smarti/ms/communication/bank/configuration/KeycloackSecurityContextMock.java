package br.blog.smarti.ms.communication.bank.configuration;

import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

/***
 * É necessário criar o mock do securityContext para sucesso dos testes, pois estão sendo usados
 * claims que devem ser acessadas através do token. Para isso, é necessário criar essa classe de
 * configuração que retorna uma instância do securityContext mockada ao invés de subir a instância
 * do securityContext da classe main.configuration.WebConfiguration. Um detalhe importante é que o
 * profile deve ser específico para o ambiente de teste, caso contrário tentará subir os dois Beans
 * do securityContext e o contexto da aplicação não irá subir.
 *
 * https://docs.spring.io/spring-security/site/docs/4.2.x/reference/html/test-method.html
 */

@Configuration
public class KeycloackSecurityContextMock {

  @Bean
  @Scope
  @Profile("test")
  KeycloakSecurityContext securityContext() {
    Map<String, Object> otherClaims = new HashMap<>();
    otherClaims.put("email", "<E-mail>");
    otherClaims.put("username", "<UserName>");

    AccessToken idToken = Mockito.mock(AccessToken.class);

    when(idToken.getOtherClaims()).thenReturn(otherClaims);

    RefreshableKeycloakSecurityContext keycloakSecurityContext =
        Mockito.mock(RefreshableKeycloakSecurityContext.class);

    when(keycloakSecurityContext.getToken()).thenReturn(idToken);

    return keycloakSecurityContext;
  }
}
