package model.entities;

import java.io.Serializable;

public class Conversa implements Comparable<Conversa>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6870208779073610029L;
	
	private int	id;
	private Mensagem mensagem;
	private Usuario participante1;
	private Usuario participante2;
		
	public Conversa(int id, Mensagem mensagem, Usuario participante1, Usuario participante2) {
		this.id = id;
		this.mensagem = mensagem;
		this.participante1 = participante1;
		this.participante2 = participante2;
	}

	public Mensagem getMensagem() {
		return mensagem;
	}
	
	public void setMensagem(Mensagem mensagem) {
		this.mensagem = mensagem;
	}
	
	public Usuario getParticipante1() {
		return participante1;
	}
	
	public void setParticipante1(Usuario participante1) {
		this.participante1 = participante1;
	}
	
	public Usuario getParticipante2() {
		return participante2;
	}
	
	public void setParticipante2(Usuario participante2) {
		this.participante2 = participante2;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int compareTo(Conversa c) {	
		return this.mensagem.getDateTime().compareTo(c.getMensagem().getDateTime());
	}	
}
