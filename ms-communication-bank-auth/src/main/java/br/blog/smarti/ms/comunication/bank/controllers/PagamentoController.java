package br.blog.smarti.ms.comunication.bank.controllers;

import java.net.URI;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.blog.smarti.ms.comunication.bank.dtos.PagamentoDto;
import br.blog.smarti.ms.comunication.bank.dtos.RetornoDto;
import br.blog.smarti.ms.comunication.bank.services.PagamentoService;

@RestController
public class PagamentoController extends AbstractController {

  private Logger LOG = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private PagamentoService pagamentoService;


  /***
   * Resource fora do escopo de segurança, portanto não é necessário token.
   * 
   * ROLE: N/A.
   */
  @RequestMapping(path = "/hello", method = RequestMethod.GET)
  public ResponseEntity<RetornoDto> status() {
    LOG.info("Rest Controller: status");
    return ResponseEntity
        .ok(new RetornoDto("msc-bank is On! Don't need authentication! :)", getUri()));
  }

  /***
   * Resource sem parâmetro para pegar o token do header (@RequestHeader), pois não há a necessidade
   * uma vez que na configuração da aplicação (webConfiguration) é gerado um Bean do contexto do
   * keycloak que contem o token, conforme é utilizado em AbstractApi.
   * 
   * ROLE: User.
   */
  @RequestMapping(value = "/api/users/1", method = RequestMethod.GET)
  public ResponseEntity<RetornoDto> getUserNoTokenAsRequestHeaderParameter() {
    LOG.info("Rest Controller: getUserNoTokenAsRequestHeaderParameter");
    String role = isAdmin() ? " (Admin)" : " (User)";
    return ResponseEntity
        .ok(new RetornoDto("Hello " + getUsername() + " - " + role, getUri()));
  }

  /***
   * Resource com parâmreto para pegar o token pelo header, apesar de não ser necessário. Conforme
   * visto no exemplo GET users/1.
   * 
   * ROLE: User.
   */
  @RequestMapping(value = "/api/users/2", method = RequestMethod.GET)
  public ResponseEntity<RetornoDto> getUserTokenAsRequestHeaderParameter(
      @RequestHeader("authorization") String authorization) {
    LOG.info("Rest Controller: getUserTokenAsRequestHeaderParameter");
    String role = isAdmin() ? " (Admin)" : " (User)";
    StringBuilder token = new StringBuilder(authorization.replace("Bearer ", ""));
    RetornoDto retorno =
        new RetornoDto("Hello " + getUsername() + " - " + getEmail() + role, getUri());
    retorno.setToken(token.toString());
    return ResponseEntity.ok(retorno);
  }

  /***
   * ROLE: Admin.
   */
  @RequestMapping(value = "/api/admins", method = RequestMethod.GET)
  public ResponseEntity<RetornoDto> getAdmin() {
    LOG.info("Rest Controller: getAdmin");
    return ResponseEntity
        .ok(new RetornoDto("Hello " + getUsername() + " - " + getEmail() + " (Admin)", getUri()));
  }


  @RequestMapping(path = "/pagamentos", method = RequestMethod.POST)
  public ResponseEntity<RetornoDto> pagamento(
      @Valid @NotNull @RequestBody PagamentoDto pagamentoDto) {
    LOG.info("Rest Controller: pagamento");

    pagamentoService.pagamento(pagamentoDto);

    RetornoDto retorno = new RetornoDto();
    retorno.setMensagem("Pagamento registrado com sucesso");

    return ResponseEntity.ok(retorno);
  }

  @RequestMapping(path = "/api/admins/pagamentos", method = RequestMethod.POST)
  public ResponseEntity<RetornoDto> pagamentoAuthenticated(
      @Valid @NotNull @RequestBody PagamentoDto pagamentoDto) {
    LOG.info("Rest Controller: pagamentoAuthenticated");

    pagamentoService.pagamento(pagamentoDto);

    RetornoDto retorno = new RetornoDto("Pagamento registrado com sucesso", getUri());
    return ResponseEntity.ok(retorno);
  }



  /***
   * Obvio que esse cara não deveria estar aqui. :)
   */
  private URI getUri() {
    return ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
  }
}
