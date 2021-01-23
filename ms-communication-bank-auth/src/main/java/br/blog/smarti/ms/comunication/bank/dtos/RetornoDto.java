package br.blog.smarti.ms.comunication.bank.dtos;

import java.net.URI;

public class RetornoDto {

  private String mensagem;
  private String uri;

  public RetornoDto() {}

  public RetornoDto(String mensagem) {
    this.mensagem = mensagem;
  }

  public RetornoDto(String mensagem, URI uri) {
    this.mensagem = mensagem;
    this.uri = uri.toString();
  }

  public String getMensagem() {
    return mensagem;
  }

  public void setMensagem(String mensagem) {
    this.mensagem = mensagem;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(URI uri) {
    this.uri = uri.toString();
  }
}
