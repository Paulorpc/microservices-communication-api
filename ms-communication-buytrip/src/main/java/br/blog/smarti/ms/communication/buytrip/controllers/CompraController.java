package br.blog.smarti.ms.communication.buytrip.controllers;

import br.blog.smarti.ms.communication.buytrip.dtos.CompraChaveDto;
import br.blog.smarti.ms.communication.buytrip.dtos.CompraDto;
import br.blog.smarti.ms.communication.buytrip.dtos.RetornoDto;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompraController {

  @Autowired private RabbitTemplate rabbitTemplate;

  @Value("${fila.saida}")
  private String nomeFila;

  @RequestMapping(path = "/hello", method = RequestMethod.GET)
  public String status() {
    return "msc-buytrip is On!";
  }

  @RequestMapping(path = "/", method = RequestMethod.POST)
  public ResponseEntity<RetornoDto> pagamento(@Valid @NotNull @RequestBody CompraDto compraDto)
      throws Exception {

    CompraChaveDto compraChaveJson = new CompraChaveDto();
    compraChaveJson.setCompraDto(compraDto);
    compraChaveJson.setChave(UUID.randomUUID().toString());

    ObjectMapper obj = new ObjectMapper();

    String json = obj.writeValueAsString(compraChaveJson);

    rabbitTemplate.convertAndSend(nomeFila, json);

    RetornoDto retorno = new RetornoDto();
    retorno.setMensagem("Compra registrada com sucesso. Aguarda a confirmação do pagamento.");
    retorno.setChavePesquisa(compraChaveJson.getChave());

    return ResponseEntity.ok(retorno);
  }
}
