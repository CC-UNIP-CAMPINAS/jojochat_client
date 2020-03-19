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
	
	public void pegaNome() {
		System.out.println(lbUser.getText());
	}
	
	public void selecionado() {
		apUserChat.setStyle("-fx-background-color: #b7b7b7");
	}
	
	public void deSeleciona() {
		apUserChat.setStyle(null);
		
	}
	
	public void clicado() {

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
}