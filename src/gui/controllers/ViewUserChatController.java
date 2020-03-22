package gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ViewUserChatController implements Initializable {
	
	public String usuario;
	
	@FXML
	private AnchorPane apUserChat;
	
	@FXML
	private Label lbUser;
	
	public void setaNomeUsuario(String nome) {
		lbUser.setText(nome);
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
		ViewCentralController.setUserParaConversar(pegaNome());
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
}