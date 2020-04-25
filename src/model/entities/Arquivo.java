package model.entities;

import java.io.File;
import java.io.Serializable;

public class Arquivo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1999029996687011836L;
	
	private File localizacaoServidor = null;
	private File localizacaoRemetente;
	private File localizacaoDestinatario = null;
	private byte[] conteudo;
	
	public Arquivo(byte[] conteudo, File localizacaoRemetente) {
		this.conteudo = conteudo;
		this.localizacaoRemetente = localizacaoRemetente;
	}
	
	public Arquivo(byte[] conteudo, File localizacaoRemetente, File localizacaoDestinatario, File localizacaoServidor) {
		this.conteudo = conteudo;
		this.localizacaoRemetente = localizacaoRemetente;
		this.setLocalizacaoServidor(localizacaoServidor);
		this.localizacaoDestinatario = localizacaoDestinatario;
	}

	public byte[] getConteudo() {
		return conteudo;
	}

	public void setConteudo(byte[] conteudo) {
		this.conteudo = conteudo;
	}

	public File getLocalizacaoRemetente() {
		return localizacaoRemetente;
	}

	public void setLocalizacaoRemetente(File localizacao) {
		this.localizacaoRemetente = localizacao;
	}

	public File getLocalizacaoServidor() {
		return localizacaoServidor;
	}

	public void setLocalizacaoServidor(File localizacaoServidor) {
		this.localizacaoServidor = localizacaoServidor;
	}

	public File getLocalizacaoDestinatario() {
		return localizacaoDestinatario;
	}

	public void setLocalizacaoDestinatario(File localizacaoDestinatario) {
		this.localizacaoDestinatario = localizacaoDestinatario;
	}
}
