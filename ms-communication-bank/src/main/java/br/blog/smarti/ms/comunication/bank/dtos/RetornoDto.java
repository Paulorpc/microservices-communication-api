package br.blog.smarti.ms.comunication.bank.dtos;

public class RetornoDto {
	
	private String mensagem;

	public RetornoDto() {
	}
	
	public RetornoDto(String message) {
		mensagem = message;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

}
