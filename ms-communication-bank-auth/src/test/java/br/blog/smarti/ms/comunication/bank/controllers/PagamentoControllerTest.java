package br.blog.smarti.ms.comunication.bank.controllers;

import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.blog.smarti.ms.comunication.bank.dtos.PagamentoDto;
import br.blog.smarti.ms.comunication.bank.services.PagamentoService;

@WebMvcTest(PagamentoController.class)
@AutoConfigureMockMvc
@ComponentScan(
    basePackageClasses = {KeycloakSecurityComponents.class, KeycloakSpringBootConfigResolver.class})
public class PagamentoControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  @MockBean
  private PagamentoService pagamentoService;

  @Autowired
  @MockBean
  private KeycloakSecurityContext securityContext;

  @Test
  public void should_get_hello() throws Exception {
    this.mvc.perform(get("/hello").contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.mensagem", containsStringIgnoringCase("msc-bank is on!")));
  }

  @Test
  public void should_post_payment() throws Exception {
    PagamentoDto pgto = new PagamentoDto();
    pgto.setNroCartao(12);
    pgto.setCodigoSegurancaCartao(123456);
    pgto.setValorCompra(new BigDecimal(200));
    ObjectMapper mapper = new ObjectMapper();

    this.mvc
        .perform(post("/pagamentos").content(mapper.writeValueAsString(pgto))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk()).andExpect(
            jsonPath("$.mensagem", containsStringIgnoringCase("pagamento registrado com sucesso")));

  }

  @Test
  public void should_post_payment_with_invalid_body() throws Exception {
    PagamentoDto pgto = new PagamentoDto();
    ObjectMapper mapper = new ObjectMapper();

    this.mvc.perform(post("/pagamentos").content(mapper.writeValueAsString(pgto))
        .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "user", roles = {"user", "admin"})
  public void should_post_authenticated_payment() throws Exception {
    PagamentoDto pgto = new PagamentoDto();
    pgto.setNroCartao(12);
    pgto.setCodigoSegurancaCartao(123456);
    pgto.setValorCompra(new BigDecimal(200));
    ObjectMapper mapper = new ObjectMapper();

    this.mvc
        .perform(post("/api/pagamentos").content(mapper.writeValueAsString(pgto))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk()).andExpect(
            jsonPath("$.mensagem", containsStringIgnoringCase("pagamento registrado com sucesso")));

  }

}
