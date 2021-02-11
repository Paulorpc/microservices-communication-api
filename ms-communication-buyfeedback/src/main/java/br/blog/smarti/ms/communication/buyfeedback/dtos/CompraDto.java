package br.blog.smarti.ms.communication.buyfeedback.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompraDto {

  private Integer codigoPassagem;

  private Integer nroCartao;

  private Integer codigoSegurancaCartao;

  private BigDecimal valorPassagem;
}
