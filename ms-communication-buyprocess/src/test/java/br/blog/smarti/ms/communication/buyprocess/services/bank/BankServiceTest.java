package br.blog.smarti.ms.communication.buyprocess.services.bank;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import br.blog.smarti.ms.communication.buyprocess.dtos.BankRetornoDto;
import br.blog.smarti.ms.communication.buyprocess.dtos.CompraChaveDto;
import br.blog.smarti.ms.communication.buyprocess.dtos.CompraDto;
import br.blog.smarti.ms.communication.buyprocess.dtos.KeycloackTokenResponseDto;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

/***
 * Para que seja mockado a variável restTemplate, ela deve ser um atributo de classe no service,
 * caso contrário é gerado uma nova instância ao carregar o método.
 */

@RunWith(MockitoJUnitRunner.class)
public class BankServiceTest {

  @Autowired @InjectMocks private BankService bankService;

  @Mock private RestTemplate restTemplate;

  @Mock private KeycloackService keycloackService;

  private CompraChaveDto compraChave;

  private BankRetornoDto bankRetorno;

  @Before
  public void setup() throws Exception {
    compraChave = new CompraChaveDto();
    CompraDto compra = new CompraDto();
    compra.setCodigoPassagem(1);
    compra.setCodigoSegurancaCartao(123456);
    compra.setNroCartao(12);
    compra.setValorPassagem(new BigDecimal(200));
    compraChave.setChave(UUID.randomUUID().toString());
    compraChave.setCompraDto(compra);

    bankRetorno = new BankRetornoDto();

    when(keycloackService.resgatarTokenUsuario(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(Mockito.mock(KeycloackTokenResponseDto.class));

    when(keycloackService.resgatarTokenUsuario("paulorpc", "1234").getAccess_token())
        .thenReturn("token");
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Test
  public void shoul_make_payment_calling_bank_module_api() throws Exception {
    bankRetorno.setMensagem("Pagamento registrado com sucesso");

    when(restTemplate.exchange(
            Mockito.isNull(),
            Mockito.<HttpMethod>any(),
            Mockito.<HttpEntity<?>>any(),
            Mockito.<Class<?>>any(),
            Mockito.<Class<?>>any()))
        .thenReturn(new ResponseEntity(bankRetorno, HttpStatus.OK));

    PagamentoRetorno pgto = bankService.pagar(compraChave);
    assertTrue(pgto.isPagamentoOK());
    assertEquals("Pagamento registrado com sucesso", pgto.getMensagem());
  }

  @Test
  public void shoul_not_make_payment_badrequest_calling_bank_api() throws Exception {
    bankRetorno.setMensagem("Pagamento registrado com sucesso");

    when(restTemplate.exchange(
            Mockito.isNull(),
            Mockito.<HttpMethod>any(),
            Mockito.<HttpEntity<?>>any(),
            Mockito.<Class<?>>any(),
            Mockito.<Class<?>>any()))
        .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Saldo insulficiente"));

    PagamentoRetorno pgto = bankService.pagar(compraChave);
    assertFalse(pgto.isPagamentoOK());
  }

  @Test(expected = HttpServerErrorException.class)
  public void shoul_not_make_payment_internalservererror_calling_bank_api() throws Exception {
    bankRetorno.setMensagem("Pagamento registrado com sucesso");

    when(restTemplate.exchange(
            Mockito.isNull(),
            Mockito.<HttpMethod>any(),
            Mockito.<HttpEntity<?>>any(),
            Mockito.<Class<?>>any(),
            Mockito.<Class<?>>any()))
        .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

    bankService.pagar(compraChave);
  }

  @Test(expected = HttpServerErrorException.class)
  public void shoul_not_make_payment_badgateway_calling_bank_api() throws Exception {
    bankRetorno.setMensagem("Pagamento registrado com sucesso");

    when(restTemplate.exchange(
            Mockito.isNull(),
            Mockito.<HttpMethod>any(),
            Mockito.<HttpEntity<?>>any(),
            Mockito.<Class<?>>any(),
            Mockito.<Class<?>>any()))
        .thenThrow(new HttpServerErrorException(HttpStatus.BAD_GATEWAY));

    bankService.pagar(compraChave);
  }
}
