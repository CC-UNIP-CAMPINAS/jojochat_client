package gui.controllers;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

import app.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import utils.Connection;

public class ViewLoginController implements Initializable {

	private static Scene sceneCentral;

	@FXML
	public Button btLogin;

	@FXML
	public TextField tfLogin;

	public void fazLogin() {
		try {

			

			Vector<String> login = new Vector<>();
			login.add("login");
			login.add(tfLogin.getText());
			Connection.saida.writeObject(login);
			Connection.saida.reset();

			Object resposta = Connection.entrada.readObject();
			if (resposta instanceof Boolean) {
				Boolean permissaoDeLogin = (Boolean) resposta;
				if (permissaoDeLogin) {
					ViewCentralController.setUser(tfLogin.getText());
					carregaTelaPrincipal();
				} else {
					System.out.println("Escolha outro usuários!");
				}
			}

		} catch (EOFException | SocketException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void carregaTelaPrincipal() {
		Parent fxmlCentral;
		try {
			fxmlCentral = FXMLLoader.load(getClass().getResource("/gui/views/ViewCentral.fxml"));
			sceneCentral = new Scene(fxmlCentral);
			Main.primaryStage.setScene(sceneCentral);
			Main.primaryStage.setWidth(245);
			Main.primaryStage.setHeight(489);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}