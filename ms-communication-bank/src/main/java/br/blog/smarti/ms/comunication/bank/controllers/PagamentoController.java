package br.blog.smarti.ms.comunication.bank.controllers;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import br.blog.smarti.ms.comunication.bank.dtos.PagamentoDto;
import br.blog.smarti.ms.comunication.bank.dtos.RetornoDto;
import br.blog.smarti.ms.comunication.bank.services.PagamentoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PagamentoController {

	@Autowired
	private PagamentoService pagamentoService;
	
	@RequestMapping(path = "/pagamento", method = RequestMethod.POST)
	public ResponseEntity<RetornoDto> pagamento(@Valid @NotNull @RequestBody PagamentoDto pagamentoDto) {

		pagamentoService.pagamento(pagamentoDto);
		
		RetornoDto retorno = new RetornoDto();
		retorno.setMensagem("Pagamento registrado com sucesso");
		
		return new ResponseEntity<RetornoDto>(retorno, HttpStatus.OK);
	}
}