package br.blog.smarti.ms.comunication.bank.controllers;

import java.net.URI;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

  @Autowired
  private PagamentoService pagamentoService;


  @RequestMapping(path = "/hello", method = RequestMethod.GET)
  public ResponseEntity<RetornoDto> status() {
    return ResponseEntity.ok(new RetornoDto("msc-bank is On! Don't need authentication! :)", getUri()));
  }

  @RequestMapping(value = "/api/user", method = RequestMethod.GET)
  public ResponseEntity<RetornoDto> getUser(@RequestHeader String Authorization) {
    String role = isAdmin() ? " (Admin)" : " (User)";
    return ResponseEntity.ok(new RetornoDto("Hello " + getUsername() + " - " + getEmail() + role, getUri()));
  }

  @RequestMapping(value = "/api/admin", method = RequestMethod.GET)
  public ResponseEntity<RetornoDto> getAdmin(@RequestHeader String Authorization) {
    return ResponseEntity.ok(new RetornoDto("Hello " + getUsername() + " - " + getEmail() + " (Admin)", getUri()));
  }
  
  
  @RequestMapping(path = "/pagamentos", method = RequestMethod.POST)
  public ResponseEntity<RetornoDto> pagamento(
      @Valid @NotNull @RequestBody PagamentoDto pagamentoDto) {

    pagamentoService.pagamento(pagamentoDto);

    RetornoDto retorno = new RetornoDto();
    retorno.setMensagem("Pagamento registrado com sucesso");

    return ResponseEntity.ok(retorno);
  }

  @RequestMapping(path = "/api/pagamentos", method = RequestMethod.POST)
  public ResponseEntity<RetornoDto> pagamentoAuthenticated(
      @Valid @NotNull @RequestBody PagamentoDto pagamentoDto) {

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
