package br.blog.smarti.ms.communication.buyprocess.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagamentoDto {

	private Integer nroCartao;
	private Integer codigoSegurancaCartao;
	private BigDecimal valorCompra;

}