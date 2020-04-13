package model.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Mensagem implements Serializable{
	
	private static final long serialVersionUID = 1605005480595465782L;
	
	private String mensagem;
	private Usuario remetente;
	private Usuario destinatario;
	private LocalDateTime dateTime;
	
	
	public Mensagem(String mensagem, Usuario remetente, Usuario destinatario, LocalDateTime dateTime) {
		this.mensagem = mensagem;
		this.remetente = remetente;
		this.destinatario = destinatario;
		this.dateTime = dateTime;
	}

	public String getMensagem() {
		return mensagem;
	}
	
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public Usuario getRemetente() {
		return remetente;
	}
	
	public void setRemetente(Usuario remetente) {
		this.remetente = remetente;
	}
	
	public Usuario getDestinatario() {
		return destinatario;
	}
	
	public void setDestinatario(Usuario destinatario) {
		this.destinatario = destinatario;
	}
	
	public LocalDateTime getDateTime() {
		return dateTime;
	}
	
	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}
	
}
