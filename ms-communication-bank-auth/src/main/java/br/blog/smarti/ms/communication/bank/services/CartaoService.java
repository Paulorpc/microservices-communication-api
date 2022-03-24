package br.blog.smarti.ms.communication.bank.services;

import br.blog.smarti.ms.communication.bank.entities.Cartao;
import br.blog.smarti.ms.communication.bank.repositories.CartaoRepository;
import java.math.BigDecimal;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartaoService {

  @Autowired private CartaoRepository cartaoRepository;

  public boolean isValido(Integer codigoSegurancaCartao, Integer nroCartao) {
    return cartaoRepository.findCartaoValido(codigoSegurancaCartao, nroCartao) > 0;
  }

  public boolean isValido2(Integer codigoSegurancaCartao, Integer nroCartao) {
    return cartaoRepository.findCartaoValidoNativeQuery(codigoSegurancaCartao, nroCartao) > 0;
  }

  public boolean isSaldoSuficiente(
      Integer codigoSegurancaCartao, Integer nroCartao, BigDecimal valorCompra) {
    return cartaoRepository.isSaldoSuficiente(codigoSegurancaCartao, nroCartao, valorCompra) > 0;
  }

  public Cartao getCartao(Integer codigoSegurancaCartao, Integer nroCartao) {
    return cartaoRepository.findCartao(codigoSegurancaCartao, nroCartao);
  }

  @Transactional
  public void atualizarSaldo(
      Integer codigoSegurancaCartao, Integer nroCartao, BigDecimal valorCompra) {
    cartaoRepository.atualizarSaldo(codigoSegurancaCartao, nroCartao, valorCompra);
  }
}
