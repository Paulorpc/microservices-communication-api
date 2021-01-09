package br.blog.smarti.ms.comunication.bank.repositories;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import br.blog.smarti.ms.comunication.bank.entities.Cartao;

public interface CartaoRepository extends Repository<Cartao, Long>{

	@Query("select count(obj.id) from Cartao obj where obj.codigoSegurancaCartao = ?1 and obj.nroCartao = ?2")
	Integer findCartaoValido(Integer codigoSegurancaCartao, Integer nroCartao);
	
	@Query( value = "select count(id) from cartao where codigo_seguranca_cartao = ?1 and nro_cartao = ?2", 
			nativeQuery = true)
	Integer findCartaoValidoNativeQuery(Integer codigoSegurancaCartao, Integer nroCartao);

	@Query("select count(obj.id) from Cartao obj where obj.codigoSegurancaCartao = ?1 and obj.nroCartao = ?2 and obj.valorCredito >= ?3")
	Integer isSaldoSuficiente(Integer codigoSegurancaCartao, Integer nroCartao, BigDecimal valorCompra);

	@Query("from Cartao obj where obj.codigoSegurancaCartao = ?1 and obj.nroCartao = ?2")
	Cartao findCartao(Integer codigoSegurancaCartao, Integer nroCartao);

	@Modifying
	@Query("update Cartao set valorCredito = (valorCredito - ?3) where codigoSegurancaCartao = ?1 and nroCartao = ?2 ")
	void atualizarSaldo(Integer codigoSegurancaCartao, Integer nroCartao, BigDecimal valorCompra);

}