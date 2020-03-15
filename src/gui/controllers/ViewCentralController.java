package gui.controllers;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import utils.Connection;

public class ViewCentralController implements Initializable {
	Socket conexao;
	
	private static Stage stageChat = new Stage();
	private static Scene sceneChat;

	static Vector<String> usuariosAtivos = new Vector<String>();

	// ObjectInputStream entrada = null;

	// ObjectOutputStream saida = null;

	@FXML
	private TreeView<String> tvUsuariosAtivos;

	public void carregaTreeView() throws IOException {
		TreeItem<String> treeItemPrincipal = new TreeItem<String>("Usuários Ativos");
		treeItemPrincipal.setExpanded(true);

		for (String usuarios : usuariosAtivos) {
			TreeItem<String> treeItemUsuario = new TreeItem<String>(usuarios);
			treeItemPrincipal.getChildren().add(treeItemUsuario);
		}
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				tvUsuariosAtivos.setRoot(treeItemPrincipal);
			}
		});
	}
	
	public void selecionaChat() {
		if (tvUsuariosAtivos.getSelectionModel().getSelectedItem() != null
				&& tvUsuariosAtivos.getSelectionModel().getSelectedItem().isLeaf()) {	
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
		                Parent fxmlChat = FXMLLoader.load(getClass().getResource("/gui/views/ViewChat.fxml"));
		                sceneChat = new Scene(fxmlChat);
		                stageChat.setScene(sceneChat);
		                stageChat.show();
		            } catch (Exception ex) {
		            	ex.printStackTrace();
		            }
				}
			});	
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			Connection conexao = new Connection("jojochatserver.hopto.org", 12345);
			ObjectOutputStream saida = new ObjectOutputStream(conexao.getConnection().getOutputStream());
			ObjectInputStream entrada = new ObjectInputStream(conexao.getConnection().getInputStream());
			System.out.println(entrada.toString());
			ServerHandler cHandler = new ServerHandler(conexao.getConnection(), entrada);
			Thread t = new Thread(cHandler);
			t.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class ServerHandler implements Runnable {

		Socket cliente;
		ObjectInputStream entrada;

		public ServerHandler(Socket cliente, ObjectInputStream entrada) {
			this.cliente = cliente;
			this.entrada = entrada;
		}

		@Override
		public void run() {
			try {
				while (true) {
					Object recebido = entrada.readObject();
					if (recebido instanceof Vector<?>) {
						ViewCentralController.usuariosAtivos = (Vector<String>) recebido;
						carregaTreeView();
					}
				}
			}

			catch (EOFException | SocketException e) {
				e.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (ClassNotFoundException e) {
			} finally {
				try {
					cliente.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
