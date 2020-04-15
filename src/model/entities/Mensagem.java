package model.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Mensagem implements Serializable, Comparable<Mensagem>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5425106074929819256L;
	
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

	@Override
	public int compareTo(Mensagem o) {	
		return this.dateTime.compareTo(o.dateTime);
	}	
}
