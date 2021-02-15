package br.blog.smarti.ms.communication.buyprocess.services.bank;

import static org.assertj.core.api.Assertions.assertThat;

import br.blog.smarti.ms.communication.buyprocess.dtos.KeycloackTokenResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class KeycloakServiceTest {

  @Autowired KeycloackService keycloakService;

  @Test
  public void should_get_keycloak_user_token() throws Exception {
    KeycloackTokenResponseDto response = keycloakService.resgatarTokenUsuario("paulorpc", "1234");
    assertThat(response.getAccess_token()).isNotBlank();
    assertThat(response.getExpires_in()).isGreaterThan(100);
    assertThat(response.getToken_type()).isEqualTo("Bearer");
  }

  @Test
  public void should_not_get_keycloak_user_token_invalid_user() throws Exception {
    try {
      keycloakService.resgatarTokenUsuario("invalid_user", "1234");
      Assertions.fail("should get exceptino, user unauthorized.");
    } catch (HttpClientErrorException e) {
      Assertions.assertThat(e.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
  }
}
