package br.blog.smarti.ms.comunication.bank.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import br.blog.smarti.ms.comunication.bank.entities.Cartao;
import br.blog.smarti.ms.comunication.bank.repositories.CartaoRepository;
import java.math.BigDecimal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(MockitoJUnitRunner.class)
public class CartaoServiceTest {

  @Autowired @InjectMocks private CartaoService cartaoService;

  @Autowired @Mock private CartaoRepository cartaoRepository;

  @Test
  public void should_be_valid() {
    when(cartaoRepository.findCartaoValido(anyInt(), anyInt())).thenReturn(1);
    assertTrue(cartaoService.isValido(123456, 12));
  }

  @Test
  public void should_not_be_valid() {
    when(cartaoRepository.findCartaoValido(anyInt(), anyInt())).thenReturn(0);
    assertFalse(cartaoService.isValido(123456, 12));
  }

  @Test
  public void should_have_money() {
    when(cartaoRepository.isSaldoSuficiente(anyInt(), anyInt(), Mockito.any(BigDecimal.class)))
        .thenReturn(1);
    assertTrue(cartaoService.isSaldoSuficiente(123456, 12, new BigDecimal(200)));
  }

  @Test
  public void should_not_have_money() {
    when(cartaoRepository.isSaldoSuficiente(anyInt(), anyInt(), Mockito.any(BigDecimal.class)))
        .thenReturn(0);
    assertFalse(cartaoService.isSaldoSuficiente(123456, 12, new BigDecimal(200)));
  }

  @Test
  public void should_have_card() {
    when(cartaoRepository.findCartao(anyInt(), anyInt())).thenReturn(new Cartao());
    assertThat(cartaoService.getCartao(123456, 12)).isNotNull();
  }

  @Test
  public void should_not_have_card() {
    when(cartaoRepository.findCartao(anyInt(), anyInt())).thenReturn(null);
    assertThat(cartaoService.getCartao(123456, 12)).isNull();
  }
}
