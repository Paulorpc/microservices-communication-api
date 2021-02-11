package br.blog.smarti.ms.communication.buyprocess.dtos;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagamentoDto {

  private Integer nroCartao;
  private Integer codigoSegurancaCartao;
  private BigDecimal valorCompra;
}
