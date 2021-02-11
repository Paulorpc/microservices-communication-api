package br.blog.smarti.ms.communication.buyfeedback.services.finalizar;

import br.blog.smarti.ms.communication.buyfeedback.dtos.CompraFinalizadaDto;
import br.blog.smarti.ms.communication.buyfeedback.entities.CompraRedis;
import br.blog.smarti.ms.communication.buyfeedback.repositories.CompraRedisRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListenerService {

  @Autowired private CompraRedisRepository compraRedisRepository;

  @RabbitListener(queues = "${fila.finalizado}")
  public void onMessage(Message message)
      throws JsonParseException, JsonMappingException, IOException {

    String json = new String(message.getBody(), "UTF-8");

    System.out.println("Mensagem recebida:" + json);

    ObjectMapper mapper = new ObjectMapper();
    CompraFinalizadaDto compraChaveDto = mapper.readValue(json, CompraFinalizadaDto.class);

    CompraRedis credis = new CompraRedis();
    credis.setId(compraChaveDto.getCompraChaveDto().getChave());
    credis.setMensagem(compraChaveDto.getMensagem());
    credis.setNroCartao(compraChaveDto.getCompraChaveDto().getCompraDto().getNroCartao());
    credis.setValorPassagem(compraChaveDto.getCompraChaveDto().getCompraDto().getValorPassagem());
    credis.setCodigoPassagem(compraChaveDto.getCompraChaveDto().getCompraDto().getCodigoPassagem());
    credis.setPagamentoOK(compraChaveDto.isPagamentoOK());

    System.out.println("Gravando no redis....");
    compraRedisRepository.save(credis);
  }
}
