package br.blog.smarti.ms.communication.buytrip.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.blog.smarti.ms.communication.buytrip.dtos.CompraDto;

@RunWith(SpringRunner.class)
@WebMvcTest(CompraController.class)
public class CompraControllerTests {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private RabbitTemplate rabbitTemplate;

	@Test
	public void should_get_hello() throws Exception {
		this.mvc.perform(get("/hello").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
				.andExpect(jsonPath("$", is("msc-buytrip is On!")));
	}

	@Test
	public void should_post_pagamento() throws JsonProcessingException, Exception {
		CompraDto compra = new CompraDto();
		compra.setNroCartao(12);
		compra.setCodigoSegurancaCartao(123456);
		compra.setCodigoPassagem(1);
		compra.setValorPassagem(new BigDecimal("200"));
		ObjectMapper mapper = new ObjectMapper();

		this.mvc.perform(
				post("/").contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsBytes(compra)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.mensagem",
						is("Compra registrada com sucesso. Aguarda a confirmação do pagamento.")));
	}
	
	@Test
	public void should_not_post_pagamento_missing_parameter() throws JsonProcessingException, Exception {
		CompraDto compra = new CompraDto();
		compra.setNroCartao(12);
		compra.setCodigoSegurancaCartao(123456);
		compra.setValorPassagem(new BigDecimal("200"));
		ObjectMapper mapper = new ObjectMapper();

		this.mvc.perform(
				post("/").contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsBytes(compra)))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$",
						is("Código da passagem é obrigatório")));
	}

}
