package br.blog.smarti.ms.communication.buyprocess.dtos;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown=true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompraDto {

	@NotNull(message="Código da passagem é obrigatório")
	private Integer codigoPassagem;
	
	@NotNull(message="Número do cartão é obrigatório")
	private Integer nroCartao;
	
	@NotNull(message="Código de segurança do cartão é obrigatório")
	private Integer codigoSegurancaCartao;
	
	@NotNull(message="Valor da compra é obrigatório")
	private BigDecimal valorPassagem;

}
