package br.blog.smarti.ms.communication.buyprocess.services.bank;

import br.blog.smarti.ms.communication.buyprocess.dtos.KeycloackTokenRequestDto;
import br.blog.smarti.ms.communication.buyprocess.dtos.KeycloackTokenResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class KeycloackService {

  private Logger LOG = LoggerFactory.getLogger(this.getClass());

  @Value("${keycloak.token.request.link}")
  private String tokenRequestLink;

  private RestTemplate restTemplate = new RestTemplate();

  /***
   * Faz uma solicitação para a API do keycloack para resgatar o token do usuário para que o mesmo
   * possa se autenticar a APIs que tenham security integrado com o keycloak, neste caso a ms-bank.
   */
  public KeycloackTokenResponseDto resgatarTokenUsuario(String username, String password)
      throws Exception {

    LOG.info("consultando api keycloak para recuperar o access token do usuário: " + username);

    KeycloackTokenRequestDto tokenRequest =
        KeycloackTokenRequestDto.builder()
            .client_id("ms-communication")
            .client_secret("33d2ebca-3988-4172-8b18-8177b8c275c5")
            .username(username)
            .password(password)
            .grant_type("password")
            .build();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    HttpEntity<MultiValueMap<String, String>> httpEntity =
        new HttpEntity<>(tokenRequest.asMultiValueMap(), headers);

    ResponseEntity<KeycloackTokenResponseDto> response = null;
    try {
      response =
          restTemplate.exchange(
              tokenRequestLink, HttpMethod.POST, httpEntity, KeycloackTokenResponseDto.class);
    } catch (HttpClientErrorException | HttpServerErrorException e) {
      LOG.error(
          "erro ao comunicar com bank api. StatusCode [{} {}]. ResponseBody [{}].",
          e.getStatusCode(),
          e.getStatusText(),
          e.getResponseBodyAsString());
      throw e;
    }

    LOG.info("API Response: " + response.toString());

    return response.getBody();
  }
}
