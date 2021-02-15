package br.blog.smarti.ms.communication.buyprocess.services.processar;

import br.blog.smarti.ms.communication.buyprocess.dtos.CompraChaveDto;
import br.blog.smarti.ms.communication.buyprocess.dtos.CompraFinalizadaDto;
import br.blog.smarti.ms.communication.buyprocess.services.bank.BankService;
import br.blog.smarti.ms.communication.buyprocess.services.bank.PagamentoRetorno;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ListenerService {

  private Logger LOG = LoggerFactory.getLogger(this.getClass());

  @Autowired private BankService bankService;

  @Autowired private RabbitTemplate rabbitTemplate;

  @Value("${fila.erro}")
  private String nomeFilaRepublicar;

  @Value("${fila.finalizado}")
  private String nomeFilaFinalizado;

  @HystrixCommand(fallbackMethod = "republicOnMessage")
  @RabbitListener(queues = "${fila.entrada}")
  public void onMessage(Message message) throws Exception {

    String json = new String(message.getBody(), "UTF-8");

    LOG.info("Mensagem recebida: {}", json);

    ObjectMapper mapper = new ObjectMapper();
    CompraChaveDto compraChaveDto = mapper.readValue(json, CompraChaveDto.class);

    PagamentoRetorno pg = bankService.pagar(compraChaveDto);

    CompraFinalizadaDto compraFinalizadaDto = new CompraFinalizadaDto();
    compraFinalizadaDto.setCompraChaveDto(compraChaveDto);
    compraFinalizadaDto.setPagamentoOK(pg.isPagamentoOK());
    compraFinalizadaDto.setMensagem(pg.getMensagem());

    org.codehaus.jackson.map.ObjectMapper obj = new org.codehaus.jackson.map.ObjectMapper();
    String jsonFinalizado = obj.writeValueAsString(compraFinalizadaDto);

    rabbitTemplate.convertAndSend(nomeFilaFinalizado, jsonFinalizado);
    LOG.info("Mensagem processada e enviada para fila de finalizado: {}", jsonFinalizado);
  }

  public void republicOnMessage(Message message)
      throws JsonParseException, JsonMappingException, IOException {
    LOG.error("Erro ao processar msg. Republicando... : {}", nomeFilaRepublicar);
    System.out.println();
    rabbitTemplate.convertAndSend(nomeFilaRepublicar, message);
  }
}
