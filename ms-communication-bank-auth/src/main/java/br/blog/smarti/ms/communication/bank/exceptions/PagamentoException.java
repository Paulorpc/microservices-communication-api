package br.blog.smarti.ms.communication.bank.exceptions;

public class PagamentoException extends RuntimeException {

  private static final long serialVersionUID = 8011193673466440726L;

  public PagamentoException(String message) {
    super(message);
  }
}