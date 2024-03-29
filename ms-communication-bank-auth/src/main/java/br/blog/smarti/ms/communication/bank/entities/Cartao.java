package br.blog.smarti.ms.communication.bank.entities;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cartao")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cartao {

  @Id @GeneratedValue private Long id;
  private Integer nroCartao;
  private Integer codigoSegurancaCartao;
  private BigDecimal valorCredito;
}
