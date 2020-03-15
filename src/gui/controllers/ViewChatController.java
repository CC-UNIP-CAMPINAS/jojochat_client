package gui.controllers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class ViewChatController implements Initializable {

	Socket cliente = null;
	ObjectOutputStream saida = null;
	ObjectInputStream entrada = null;
	ArrayList<String> usuariosAtivos = new ArrayList<>();

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
		String mensagem = taEscritura.getText();
		try {
			saida.writeObject(LocalDate.now() + " - " + mensagem);
			gravaMensagemTextArea(LocalDate.now() + " - " + mensagem);
			taEscritura.clear();


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void gravaMensagemTextArea(String mensagem) {
		String historico = taMensagens.getText();
		historico += mensagem + "\n\n";
		taMensagens.setText(historico);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			cliente = new Socket("localhost", 12345);
			saida = new ObjectOutputStream(cliente.getOutputStream());
			entrada = new ObjectInputStream(cliente.getInputStream());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}