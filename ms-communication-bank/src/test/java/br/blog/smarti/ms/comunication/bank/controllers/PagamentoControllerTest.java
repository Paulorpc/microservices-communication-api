package br.blog.smarti.ms.comunication.bank.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.blog.smarti.ms.comunication.bank.dtos.PagamentoDto;
import br.blog.smarti.ms.comunication.bank.services.PagamentoService;

@RunWith(SpringRunner.class)
@WebMvcTest(PagamentoController.class)
public class PagamentoControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	@MockBean
	private PagamentoService pagamentoService;

	@Test
	public void should_get_hello() throws Exception {
		this.mvc.perform(get("/hello").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
				.andExpect(jsonPath("$", is("msc-bank is On!")));
	}

	@Test
	public void should_post_payment() throws Exception {
		PagamentoDto pgto = new PagamentoDto();
		pgto.setNroCartao(12);
		pgto.setCodigoSegurancaCartao(123456);
		pgto.setValorCompra(new BigDecimal(200));
		ObjectMapper mapper = new ObjectMapper();

		this.mvc.perform(post("/pagamento").content(mapper.writeValueAsString(pgto))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
				.andExpect(jsonPath("$.mensagem", is("Pagamento registrado com sucesso")));

	}

	@Test
	public void should_post_payment_with_invalid_body() throws Exception {
		PagamentoDto pgto = new PagamentoDto();
		ObjectMapper mapper = new ObjectMapper();

		this.mvc.perform(post("/pagamento").content(mapper.writeValueAsString(pgto))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isBadRequest());
	}

}
