package model.entities;

import java.io.File;
import java.io.Serializable;

public class FormularioCadastro implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4087405652085573517L;
	
	private String nomeUsuario;
	private String login;
	private String senha;
	private File nomeArquivo;
	private byte[] imgPerfil;
	
	public FormularioCadastro(String nomeUsuario, String login, String senha, byte[] imgPerfil, File nomeArquivo) {
		this.nomeUsuario = nomeUsuario;
		this.login = login;
		this.senha = senha;
		this.imgPerfil = imgPerfil;
		this.setNomeArquivo(nomeArquivo);
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}
	
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getSenha() {
		return senha;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public byte[] getImgPerfil() {
		return imgPerfil;
	}
	
	public void setImgPerfil(byte[] imgPerfil) {
		this.imgPerfil = imgPerfil;
	}

	public File getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(File nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
}
