package gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class ViewChatController implements Initializable {


	@FXML
	public TextArea taEscritura;

	@FXML
	public TextArea taMensagens;
	
	@FXML
	public ListView<String> lvUsuarios;

	
	@FXML
	public Button btEnviar;

	@FXML
	public void enviaMensagem() {
		
	}

	public void gravaMensagemTextArea(String mensagem) {
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
}