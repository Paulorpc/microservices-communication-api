package br.blog.smarti.ms.comunication.bank.services;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import br.blog.smarti.ms.comunication.bank.dtos.PagamentoDto;
import br.blog.smarti.ms.comunication.bank.exceptions.PagamentoException;
import br.blog.smarti.ms.comunication.bank.repositories.PagamentoRepository;
import java.math.BigDecimal;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(MockitoJUnitRunner.class)
public class PagamentoServiceTest {

  @Autowired @InjectMocks private PagamentoService pgtoService;

  @Autowired @Mock private CartaoService cartaoService;

  @Autowired @Mock private PagamentoRepository pgtoRepository;

  @Test
  public void should_register_payment() {

    when(cartaoService.isValido(anyInt(), anyInt())).thenReturn(true);
    when(cartaoService.isSaldoSuficiente(anyInt(), anyInt(), Mockito.any(BigDecimal.class)))
        .thenReturn(true);

    PagamentoDto pgto = new PagamentoDto();
    pgto.setCodigoSegurancaCartao(123456);
    pgto.setNroCartao(12);
    pgto.setValorCompra(new BigDecimal(200));

    pgtoService.pagamento(pgto);
  }

  @Test
  public void should_not_register_payment_invalid_card() {

    when(cartaoService.isValido(anyInt(), anyInt())).thenReturn(false);

    PagamentoDto pgto = new PagamentoDto();
    pgto.setCodigoSegurancaCartao(123456);
    pgto.setNroCartao(12);
    pgto.setValorCompra(new BigDecimal(200));

    try {
      pgtoService.pagamento(pgto);
      Assertions.fail("Should thrown PagamentoExceptions");
    } catch (PagamentoException e) {
    }
  }

  @Test
  public void should_not_register_payment_no_money() {

    when(cartaoService.isValido(anyInt(), anyInt())).thenReturn(true);
    when(cartaoService.isSaldoSuficiente(anyInt(), anyInt(), Mockito.any(BigDecimal.class)))
        .thenReturn(false);

    PagamentoDto pgto = new PagamentoDto();
    pgto.setCodigoSegurancaCartao(123456);
    pgto.setNroCartao(12);
    pgto.setValorCompra(new BigDecimal(200));

    try {
      pgtoService.pagamento(pgto);
      Assertions.fail("Should thrown PagamentoExceptions");
    } catch (PagamentoException e) {
    }
  }
}
