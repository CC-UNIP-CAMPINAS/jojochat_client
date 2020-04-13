package gui.controllers;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

import app.Main;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.entities.Usuario;
import utils.AlertUtils;
import utils.ConnectionUtils;
import utils.CryptUtils;

public class ViewLoginController implements Initializable {

	private static Scene sceneCentral;
	boolean parada = true;

	@FXML
	public Button btLogin;

	@FXML
	public TextField tfLogin;

	@FXML
	public PasswordField pfSenha;

	@FXML
	public ImageView imgOlho;

	@FXML
	public ProgressIndicator piCarregando;

	@SuppressWarnings("unchecked")
	public void fazLogin() {
		try {
			Vector<String> login = new Vector<>();
			login.add("login");
			login.add(tfLogin.getText());
			login.add(CryptUtils.sha256(pfSenha.getText()));
			ConnectionUtils.saida.writeObject(login);
			ConnectionUtils.saida.reset();

			piCarregando.setVisible(true);
			parada = true;

			Task<Void> tarefa = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					while (parada) {
						Thread.sleep(0);
					}
					piCarregando.setVisible(false);
					return null;
				}
			};

			Platform.runLater(() -> {
				Thread t = new Thread(tarefa);
				t.start();
			});

			Task<Void> acaoCarregarLogin = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					Vector<Object> resposta = (Vector<Object>) ConnectionUtils.entrada.readObject();
					Boolean permissaoDeLogin = (Boolean) resposta.get(0);
					if (permissaoDeLogin) {
						ViewCentralController.setUser((Usuario) resposta.get(1));
						Platform.runLater(() -> {
							carregaTelaPrincipal();
						});

					} else {
						AlertUtils.showNotificacaoErroLogin(resposta.size());
					}

					parada = false;

					return null;
				}
			};

			Platform.runLater(() -> {
				Thread t = new Thread(acaoCarregarLogin);
				t.start();
			});

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
			Main.primaryStage.setMaximized(true);
			Main.primaryStage.setMinWidth(872);
			Main.primaryStage.setMinHeight(570);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}