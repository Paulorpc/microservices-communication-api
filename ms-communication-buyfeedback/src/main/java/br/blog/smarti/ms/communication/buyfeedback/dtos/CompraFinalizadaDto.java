package br.blog.smarti.ms.communication.buyfeedback.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompraFinalizadaDto {

    private CompraChaveDto compraChaveDto;
    private String mensagem;
    private boolean pagamentoOK;

}