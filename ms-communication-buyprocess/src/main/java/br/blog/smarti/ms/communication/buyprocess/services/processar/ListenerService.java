package br.blog.smarti.ms.communication.buyprocess.services.processar;

import java.io.IOException;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import br.blog.smarti.ms.communication.buyprocess.dtos.CompraChaveDto;
import br.blog.smarti.ms.communication.buyprocess.dtos.CompraFinalizadaDto;
import br.blog.smarti.ms.communication.buyprocess.services.bank.BankService;
import br.blog.smarti.ms.communication.buyprocess.services.bank.PagamentoRetorno;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ListenerService {

	@Autowired
	private BankService bank;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Value("${fila.entrada}")
	private String nomeFilaRepublicar;

	@Value("${fila.finalizado}")
	private String nomeFilaFinalizado;

	@HystrixCommand(fallbackMethod = "republicOnMessage")
	@RabbitListener(queues="${fila.entrada}")
    public void onMessage(Message message) throws JsonParseException, JsonMappingException, IOException  {
		
		String json = new String(message.getBody(), "UTF-8");
		
		System.out.println("Mensagem recebida:"+json);
		
		ObjectMapper mapper = new ObjectMapper();
		CompraChaveDto compraChaveDto = mapper.readValue(json, CompraChaveDto.class);

		PagamentoRetorno pg = bank.pagar(compraChaveDto);

		CompraFinalizadaDto compraFinalizadaDto = new CompraFinalizadaDto();
		compraFinalizadaDto.setCompraChaveDto(compraChaveDto);
		compraFinalizadaDto.setPagamentoOK(pg.isPagamentoOK());
		compraFinalizadaDto.setMensagem(pg.getMensagem());

		org.codehaus.jackson.map.ObjectMapper obj = new org.codehaus.jackson.map.ObjectMapper();
		String jsonFinalizado = obj.writeValueAsString(compraFinalizadaDto);

		rabbitTemplate.convertAndSend(nomeFilaFinalizado, jsonFinalizado);
    }

	public void republicOnMessage(Message message) throws JsonParseException, JsonMappingException, IOException  {
		System.out.println("Republicando mensagem...");
		rabbitTemplate.convertAndSend(nomeFilaRepublicar, message);
	}
}