package model.entities;

import java.io.Serializable;

import javafx.scene.image.Image;

public class Usuario implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1423238495738038990L;
	
	private String nomeDeExibicao;
	private transient Image fotoDePerfil;
	private String usuario;
	private int id;
	
	public Usuario(int id, String nomeDeExibicao, String usuario, Image fotoDePerfil) {
		this.nomeDeExibicao = nomeDeExibicao;
		this.usuario = usuario;
		this.id = id;
		this.setFotoDePerfil(fotoDePerfil);
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

	public Image getFotoDePerfil() {
		return fotoDePerfil;
	}

	public void setFotoDePerfil(Image fotoDePerfil) {
		this.fotoDePerfil = fotoDePerfil;
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
