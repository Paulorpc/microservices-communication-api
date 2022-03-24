package br.blog.smarti.ms.communication.bank.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.blog.smarti.ms.communication.bank.ApiTest;
import br.blog.smarti.ms.communication.bank.dtos.PagamentoDto;
import br.blog.smarti.ms.communication.bank.services.PagamentoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@AutoConfigureMockMvc
public class PagamentoControllerTest extends ApiTest {

  @Autowired @MockBean private PagamentoService pagamentoService;

  @Test
  public void should_get_hello() throws Exception {
    this.mvc
        .perform(get("/hello").contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.mensagem", containsStringIgnoringCase("msc-bank is on!")));
  }

  @Test
  @WithMockUser(
      username = "user",
      roles = {"user", "admin"})
  public void should_get_api_user1() throws Exception {
    this.mvc
        .perform(get("/api/users/1").contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.mensagem", containsStringIgnoringCase("hello")))
        .andExpect(jsonPath("$.mensagem", containsStringIgnoringCase("user")));
  }

  @Test
  @Disabled // desabilitado pq é preciso atualizar o token para rodar o teste
  @WithMockUser(
      username = "user",
      roles = {"user", "admin"})
  public void should_get_api_user2() throws Exception {
    String token =
        "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJmOHIyVlVfWUpiZTBLWjFzV0ZyNWJvTUxjaFBwcTR5ejY3eG5tNzM3WEZRIn0.eyJleHAiOjE2MTMwMDc0MzIsImlhdCI6MTYxMzAwNjUzMiwianRpIjoiZjAxNzBhMTktYTNkYS00MWI2LWJjYmMtNDVlMjlkYjcyM2FlIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2F1dGgvcmVhbG1zL3NtYXJ0aSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI5ZWYyZjFkMi02NjMzLTQ3NDQtOThjMy1iY2EzNDQzYTgxNmIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJtcy1jb21tdW5pY2F0aW9uIiwic2Vzc2lvbl9zdGF0ZSI6ImNiMWY4YjE5LWZkNWItNDdiYi04MjZmLTE3ODQxMjc4NDlhYiIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cDovL2xvY2FsaG9zdDo4MDkwIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsImFkbWluIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJtcy1jb21tdW5pY2F0aW9uIjp7InJvbGVzIjpbImFkbWluIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJQYXVsbyBDZXphciIsInByZWZlcnJlZF91c2VybmFtZSI6InBhdWxvcnBjIiwiZ2l2ZW5fbmFtZSI6IlBhdWxvIiwiZmFtaWx5X25hbWUiOiJDZXphciIsImVtYWlsIjoicGF1bG9ycGNAZ21haWwuY29tIn0.nmZ8RedEb0hXRU9RH8FrkR2fUH46GXkckCoR54_fOT0mVaApyzIHQSUsVOTs8FOI-yJf8Epu4A79d6_6oOaaECigt41iIMwi12geSyzu3bfMIK6pe68pb0GZnvDQT1U9FV92zxPpE4dVIk1xByKimJ7lzYW3vR1q9qa4Iy_oQdOPLZ1Ipe0KonUhHVvsh4uZCrkE8-wPrzPh4PEKvWRBDgOm4gSa6nI3p62GWTDFaoKWUvz5Wq-2SIoy_DsqEzrYi2JzFwCA-kBnQdfJujL2z-XCxs27B5UkMYuiPcEsXolx4_XGKoKcle0MaqK59LO1RqGuBQgst0l5sR9cvth3gw";
    this.mvc
        .perform(
            get("/api/users/2")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.mensagem", containsStringIgnoringCase("hello")))
        .andExpect(jsonPath("$.mensagem", containsStringIgnoringCase("user")))
        .andExpect(jsonPath("$.token", containsString(token)));
  }

  @Test
  @WithMockUser(
      username = "user",
      roles = {"admin"})
  public void should_get_api_admin() throws Exception {
    this.mvc
        .perform(get("/api/admins").contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.mensagem", containsStringIgnoringCase("hello")))
        .andExpect(jsonPath("$.mensagem", containsStringIgnoringCase("admin")));
  }

  @Test
  @WithMockUser(
      username = "user",
      roles = {"user"})
  public void should_not_get_api_admin_invalid_role() throws Exception {
    this.mvc
        .perform(get("/api/admins").contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isForbidden());
  }

  @Test
  public void should_post_payment() throws Exception {
    PagamentoDto pgto = new PagamentoDto();
    pgto.setNroCartao(12);
    pgto.setCodigoSegurancaCartao(123456);
    pgto.setValorCompra(new BigDecimal(200));
    ObjectMapper mapper = new ObjectMapper();

    this.mvc
        .perform(
            post("/pagamentos")
                .content(mapper.writeValueAsString(pgto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$.mensagem", containsStringIgnoringCase("pagamento registrado com sucesso")));
  }

  @Test
  public void should_post_payment_with_invalid_body() throws Exception {
    PagamentoDto pgto = new PagamentoDto();
    ObjectMapper mapper = new ObjectMapper();

    this.mvc
        .perform(
            post("/pagamentos")
                .content(mapper.writeValueAsString(pgto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(
      username = "user",
      roles = {"admin"})
  public void should_post_authenticated_payment() throws Exception {
    PagamentoDto pgto = new PagamentoDto();
    pgto.setNroCartao(12);
    pgto.setCodigoSegurancaCartao(123456);
    pgto.setValorCompra(new BigDecimal(200));
    ObjectMapper mapper = new ObjectMapper();

    this.mvc
        .perform(
            post("/api/admins/pagamentos")
                .content(mapper.writeValueAsString(pgto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$.mensagem", containsStringIgnoringCase("pagamento registrado com sucesso")));
  }

  @Test
  @WithMockUser(
      username = "user",
      roles = {"user"})
  public void should_not_post_authenticated_payment_invalid_role() throws Exception {
    PagamentoDto pgto = new PagamentoDto();
    pgto.setNroCartao(12);
    pgto.setCodigoSegurancaCartao(123456);
    pgto.setValorCompra(new BigDecimal(200));
    ObjectMapper mapper = new ObjectMapper();

    this.mvc
        .perform(
            post("/api/admins/pagamentos")
                .content(mapper.writeValueAsString(pgto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isForbidden());
  }
}
