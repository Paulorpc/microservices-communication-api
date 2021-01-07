package br.blog.smarti.ms.comunication.bank.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.blog.smarti.ms.comunication.bank.dtos.PagamentoDto;
import br.blog.smarti.ms.comunication.bank.entities.Pagamento;
import br.blog.smarti.ms.comunication.bank.exceptions.PagamentoException;
import br.blog.smarti.ms.comunication.bank.repositories.PagamentoRepository;

@Service
public class PagamentoService{

	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private CartaoService cartaoService;
	
	@Transactional
	public void pagamento(PagamentoDto pagamentoDto){
	
		if( !cartaoService.isValido(pagamentoDto.getCodigoSegurancaCartao(), pagamentoDto.getNroCartao()) ){
			throw new PagamentoException("Cartão inválido.");
		}
		
		if( !cartaoService.isSaldoSuficiente(pagamentoDto.getCodigoSegurancaCartao(), pagamentoDto.getNroCartao(), pagamentoDto.getValorCompra()) ){
			throw new PagamentoException("Cartão não possui saldo suficiente.");
		}

		Pagamento pagamento = new Pagamento();
		pagamento.setValorCompra(pagamentoDto.getValorCompra());
		pagamento.setCartao(cartaoService.getCartao(pagamentoDto.getCodigoSegurancaCartao(), pagamentoDto.getNroCartao()));
		
		pagamentoRepository.save(pagamento);
		
		cartaoService.atualizarSaldo(pagamentoDto.getCodigoSegurancaCartao(), pagamentoDto.getNroCartao(), pagamentoDto.getValorCompra());
	}
	
}