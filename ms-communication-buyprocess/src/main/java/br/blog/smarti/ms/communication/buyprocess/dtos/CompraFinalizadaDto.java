package br.blog.smarti.ms.communication.buyprocess.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompraFinalizadaDto {

  private CompraChaveDto compraChaveDto;
  private String mensagem;
  private boolean pagamentoOK;
}
