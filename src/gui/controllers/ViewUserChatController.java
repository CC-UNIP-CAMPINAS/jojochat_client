package gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import model.entities.Colecao;
import model.entities.Conversa;
import model.entities.Mensagem;
import model.entities.Usuario;
import utils.ConnectionUtils;

public class ViewUserChatController implements Initializable {
	
	public Usuario usuario;
	public Conversa conversa;
	private int numeroMensagens = 0;
	
	@FXML
	private AnchorPane apUserChat;
	
	@FXML
	private Label lbUser;
	
	@FXML
	private Label lbMensagem;
	
	@FXML
    private Circle circuloNumeroMensagens;

    @FXML
    private Label lbNumeroMensagens;

	
	public void setaUsuario(Usuario usuario) {
		this.usuario = usuario;
		lbUser.setText(usuario.getNomeDeExibicao());
	}
	
	public void setaConversa(Conversa conversa) {
		this.conversa = conversa;
	}
	
	public void setaMensagem(Mensagem mensagem) {
		if(mensagem.getArquivo() != null) {
			lbMensagem.setText("Arquivo");
		}
		else {
			lbMensagem.setText(mensagem.getMensagem());
		}
	}
	
	public String pegaNome() {
		return lbUser.getText();
	}
	
	public void selecionado() {
		apUserChat.setStyle("-fx-background-color: #a7a6a8");
	}
	
	public void deSeleciona() {
		apUserChat.setStyle(null);
		
	}
	
	public void clicado() {
		Colecao.associaConversaComChat();
		ViewCentralController.setUserParaConversar(usuario, conversa);
		requisitaHistoricoConversa();
		//zeraNumeroMensagens();
	}
	
	
	public void requisitaHistoricoConversa() {
		Vector<Object> requisicao = new Vector<>();
		
		requisicao.add("historico");
		requisicao.add(ViewCentralController.getUser());
		requisicao.add(ViewCentralController.getUserParaConversar());

		try {
			ConnectionUtils.saida.writeObject(requisicao);
			ConnectionUtils.saida.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ViewUserChatController other = (ViewUserChatController) obj;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}
	
	public void aumentaNumeroMensagens() {
		numeroMensagens = Integer.parseInt(lbNumeroMensagens.getText());
		numeroMensagens++;
		lbNumeroMensagens.setVisible(true);
		circuloNumeroMensagens.setVisible(true);
		lbNumeroMensagens.setText(""+numeroMensagens);
		
	}
	
	public void zeraNumeroMensagens() {
		lbNumeroMensagens.setText("0");
		lbNumeroMensagens.setVisible(false);
		circuloNumeroMensagens.setVisible(false);
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
}