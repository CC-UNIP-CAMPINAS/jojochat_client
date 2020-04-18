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
import model.entities.Conversa;
import model.entities.Mensagem;
import model.entities.Usuario;
import utils.ConnectionUtils;

public class ViewUserChatController implements Initializable {
	
	public Usuario usuario = null;
	public Conversa conversa = null;
	
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
		lbMensagem.setText(mensagem.getMensagem());
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
		ViewCentralController.setUserParaConversar(usuario, conversa);
		requisitaHistoricoConversa();
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
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
}