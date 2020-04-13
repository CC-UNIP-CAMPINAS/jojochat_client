package model.entities;

import java.io.Serializable;

public class Usuario implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1423238495738038990L;
	
	private String nomeDeExibicao;
	private String usuario;
	private int id;
	
	public Usuario(int id, String nomeDeExibicao, String usuario) {
		this.nomeDeExibicao = nomeDeExibicao;
		this.usuario = usuario;
		this.id = id;
	}
	
	public String getUsuario() {
		return usuario;
	}
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getNomeDeExibicao() {
		return nomeDeExibicao;
	}
	
	public void setNomeDeExibicao(String nomeDeExibicao) {
		this.nomeDeExibicao = nomeDeExibicao;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
