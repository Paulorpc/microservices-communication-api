package br.blog.smarti.ms.communication.buyprocess.services.processar;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import br.blog.smarti.ms.communication.buyprocess.dtos.CompraChaveDto;
import br.blog.smarti.ms.communication.buyprocess.dtos.CompraDto;
import br.blog.smarti.ms.communication.buyprocess.services.bank.BankService;
import br.blog.smarti.ms.communication.buyprocess.services.bank.PagamentoRetorno;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(MockitoJUnitRunner.class)
public class ListenerServiceTest {

  @Autowired @InjectMocks private ListenerService listener;

  @Mock private BankService bank;

  @Mock private RabbitTemplate rabbitTemplate;

  @Rule public ExpectedException expectedException = ExpectedException.none();

  CompraChaveDto compraChave = new CompraChaveDto();
  CompraDto compra = new CompraDto();
  Message message;

  @Before
  public void setup() throws JsonProcessingException {
    compra.setCodigoPassagem(1);
    compra.setCodigoSegurancaCartao(123456);
    compra.setNroCartao(12);
    compra.setValorPassagem(new BigDecimal(200));
    compraChave.setChave(UUID.randomUUID().toString());
    compraChave.setCompraDto(compra);

    ObjectMapper mapper = new ObjectMapper();
    message = new Message(mapper.writeValueAsBytes(compraChave), new MessageProperties());
  }

  @Test
  public void receive_message_finished_payment() throws Exception {
    when(bank.pagar(compraChave)).thenReturn(new PagamentoRetorno("sucesso", true));

    String eMsg = "json serializado e pronto pra envio para fila";
    expectedException.expectMessage(eMsg);
    doThrow(new RuntimeException(eMsg))
        .when(rabbitTemplate)
        .convertAndSend(Mockito.isNull(), Mockito.anyString());

    listener.onMessage(message);
  }

  @Test
  public void receive_message_not_fineshed_payment_bank_api_fail() throws Exception {
    when(bank.pagar(compraChave)).thenReturn(new PagamentoRetorno("error message", false));

    String eMsg = "json serializado e pronto pra envio para fila";
    expectedException.expectMessage(eMsg);
    doThrow(new RuntimeException(eMsg))
        .when(rabbitTemplate)
        .convertAndSend(Mockito.isNull(), Mockito.anyString());

    listener.onMessage(message);
  }

  @Test
  public void receive_invalid_message_not_finished_payment() throws Exception {
    Message message = new Message("{json: invalid object}".getBytes(), new MessageProperties());

    // should have parse error in mapper.readValue()
    expectedException.expect(JsonParseException.class);

    listener.onMessage(message);
  }
}
