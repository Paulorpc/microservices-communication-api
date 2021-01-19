package br.blog.smarti.ms.communication.buyprocess.services.bank;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
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
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Charsets;

import br.blog.smarti.ms.communication.buyprocess.dtos.BankRetornoDto;
import br.blog.smarti.ms.communication.buyprocess.dtos.CompraChaveDto;
import br.blog.smarti.ms.communication.buyprocess.dtos.CompraDto;

/***
 * Para que seja mockado a variável restTemplate, ela deve ser um atributo de
 * classe no service, caso contrário é gerado uma nova instância ao carregar o
 * método, ou seja, não carrega a instância mockada do teste, assim tentando
 * fazer a conexão com a API.
 * 
 * Outro detalhe é que quando a insância 'link' está sendo atribuido como um
 * value do propertie (application.propertie), é carregado valor null, pois como
 * não é carregado o contexto do sprinboot completo não consegue receber o valor
 * da var. Estou buscando alternativas para tests sem carregar o contexto do
 * springboot para ter melhor performance e evitar dependências externas.
 * 
 * '@SpringBootTest' é usado para teste de integração, ou seja, não é usado
 * mock. Por isso, se usar essa notação, os mocks não irão funcionar.
 * 
 * Usando o conjunto de anotações abaixo, consigo carregar o contexto da classe
 * e carregar o link através do properties, porém não carrega os mocks.
 * @RunWith(SpringRunner.class) 
 * @ContextConfiguration(classes = {BankService.class})
 * @TestPropertySource(locations = {"classpath:application.properties"})
 *
 */
@RunWith(MockitoJUnitRunner.class)
//@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = {BankService.class})
//@TestPropertySource(locations = {"classpath:application.properties"})
public class BankServiceTest {

	@Autowired
	@InjectMocks
	private BankService bankService;

	@Mock
	private RestTemplate restTemplate;

	private CompraChaveDto compraChave;

	private BankRetornoDto bankRetorno;

	@Before
	public void beforeEach() {
		compraChave = new CompraChaveDto();

		CompraDto compra = new CompraDto();
		compra.setCodigoPassagem(1);
		compra.setCodigoSegurancaCartao(123456);
		compra.setNroCartao(12);
		compra.setValorPassagem(new BigDecimal(200));

		compraChave.setChave(UUID.randomUUID().toString());
		compraChave.setCompraDto(compra);

		bankRetorno = new BankRetornoDto();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void shoul_make_payment_calling_bank_module_api() throws IOException {
		bankRetorno.setMensagem("Pagamento registrado com sucesso");

		when(restTemplate.exchange(Mockito.isNull(), Mockito.<HttpMethod>any(), Mockito.<HttpEntity<?>>any(),
				Mockito.<Class<?>>any(), Mockito.<Class<?>>any()))
						.thenReturn(new ResponseEntity(bankRetorno, HttpStatus.OK));

//		Outra forma de fazer o mesmo mock. 
//		ResponseEntity<BankRetornoDto> retorno = ResponseEntity.ok(bankRetorno);
//		
//		doReturn(retorno).when(restTemplate).exchange(
//				  Mockito.isNull(),
//	              Mockito.<HttpMethod> any(),
//	              Mockito.<HttpEntity<?>> any(),
//	              Mockito.<Class<?>> any(),
//	              Mockito.<Class<?>> any()); 

		PagamentoRetorno pgto = bankService.pagar(compraChave);
		assertTrue(pgto.isPagamentoOK());
		assertEquals("Pagamento registrado com sucesso", pgto.getMensagem());
	}

	@Test
	public void shoul_not_make_payment_calling_bank_module_api() throws IOException {
		bankRetorno.setMensagem("Pagamento não registrado");

		HttpClientErrorException e = new HttpClientErrorException(HttpStatus.BAD_REQUEST,
				HttpStatus.BAD_REQUEST.toString(), bankRetorno.getMensagem().getBytes(), Charsets.UTF_8);

		when(restTemplate.exchange(Mockito.isNull(), Mockito.<HttpMethod>any(), Mockito.<HttpEntity<?>>any(),
				Mockito.<Class<?>>any(), Mockito.<Class<?>>any())).thenThrow(e);

		PagamentoRetorno pgto = bankService.pagar(compraChave);
		assertFalse(pgto.isPagamentoOK());
		assertEquals("Pagamento não registrado", pgto.getMensagem());
	}

}
