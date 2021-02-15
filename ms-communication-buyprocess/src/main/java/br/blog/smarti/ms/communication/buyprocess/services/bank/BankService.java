package br.blog.smarti.ms.communication.buyprocess.services.bank;

import br.blog.smarti.ms.communication.buyprocess.dtos.BankRetornoDto;
import br.blog.smarti.ms.communication.buyprocess.dtos.CompraChaveDto;
import br.blog.smarti.ms.communication.buyprocess.dtos.PagamentoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class BankService {

  private Logger LOG = LoggerFactory.getLogger(this.getClass());

  @Value("${bank.link.auth}")
  private String bankAuthLink;

  @Autowired private KeycloackService keycloackService;

  private RestTemplate restTemplate = new RestTemplate();

  public PagamentoRetorno pagar(CompraChaveDto compraChaveDto) throws Exception {

    LOG.info("realizando pagamento de compra: {}", compraChaveDto.toString());

    PagamentoDto json = new PagamentoDto();
    json.setNroCartao(compraChaveDto.getCompraDto().getNroCartao());
    json.setCodigoSegurancaCartao(compraChaveDto.getCompraDto().getCodigoSegurancaCartao());
    json.setValorCompra(compraChaveDto.getCompraDto().getValorPassagem());

    try {
      StringBuilder token = new StringBuilder("Bearer ");
      token.append(keycloackService.resgatarTokenUsuario("paulorpc", "1234").getAccess_token());

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.add("Authorization", token.toString());
      HttpEntity<PagamentoDto> httpEntity = new HttpEntity<>(json, headers);

      ResponseEntity<BankRetornoDto> bankRetorno =
          restTemplate.exchange(bankAuthLink, HttpMethod.POST, httpEntity, BankRetornoDto.class);

      return new PagamentoRetorno(bankRetorno.getBody().getMensagem(), true);

    } catch (HttpClientErrorException | HttpServerErrorException e) {
      if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
        LOG.warn(
            "Comunicação com Bank API realizada, mas retornou falha: {}",
            e.getResponseBodyAsString());
        return new PagamentoRetorno(e.getResponseBodyAsString(), false);
      }
      LOG.error("Erro ao comunicar com bank api: {}", e.getResponseBodyAsString());
      throw e;
    } catch (RuntimeException e) {
      throw e;
    }
  }
}
