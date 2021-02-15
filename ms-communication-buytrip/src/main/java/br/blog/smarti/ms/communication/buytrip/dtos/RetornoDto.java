package br.blog.smarti.ms.communication.buytrip.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetornoDto {

  private String mensagem;
  private String chavePesquisa;
}
