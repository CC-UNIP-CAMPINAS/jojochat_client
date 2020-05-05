package model.entities;

import java.util.Vector;

import gui.controllers.ViewUserChatController;

public class Colecao {
	public static Vector<Usuario> usuariosAtivos = new Vector<>();
	public static Vector<ViewUserChatController> chatsAtivos = new Vector<>();
	public static Vector<ViewUserChatController> chatsNaoAtivos = new Vector<>();
	public static Vector<Vector<?>> filaRequisicoes = new Vector<>();
	
	public static void associaConversaComChat() {
		for (ViewUserChatController chatNaoAtivo : Colecao.chatsNaoAtivos) {
			for (ViewUserChatController chatAtivo : Colecao.chatsAtivos) {
				if(chatAtivo.equals(chatNaoAtivo)) {
					chatNaoAtivo.setaConversa(chatAtivo.conversa);
				}
			}
		}
	}
}
