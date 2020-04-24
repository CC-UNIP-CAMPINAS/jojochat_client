package model.entities;

import java.io.File;
import java.io.Serializable;

public class Arquivo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1999029996687011836L;
	
	private File localizacao;
	private byte[] conteudo;
	
	public Arquivo(byte[] conteudo, File localizacao) {
		super();
		this.conteudo = conteudo;
		this.setLocalizacao(localizacao);
	}

	public byte[] getConteudo() {
		return conteudo;
	}

	public void setConteudo(byte[] conteudo) {
		this.conteudo = conteudo;
	}

	public File getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(File localizacao) {
		this.localizacao = localizacao;
	}
}
