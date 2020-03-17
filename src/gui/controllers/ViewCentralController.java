package gui.controllers;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

import app.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import utils.AlertUtils;
import utils.Connection;

public class ViewCentralController implements Initializable {
	Socket conexao;
	private static String user;

	static Vector<String> usuariosAtivos = new Vector<String>();

	@FXML
	public TextArea taEscritura;
	
	@FXML
	public Tab tabUsuario;

	@FXML
	public TextArea taMensagens;
	
	@FXML
	public ListView<String> lvUsuarios;

	@FXML
	public Button btEnviar;
	
	@FXML
	public Button btFecharAbaChat;
	
	@FXML
	private TreeView<String> tvUsuariosAtivos;
	
	@FXML
	private StackPane stpChat;
	
	@FXML
	private Label lbUsuarioAtivo;

	//vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv-=Gets/Sets=-vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv//
	
	public static String getUser() {
		return user;
	}

	public static void setUser(String user) {
		ViewCentralController.user = user;
	}
	
	//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^-=Gets/Sets=- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^//
	
	//vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv-=Ações de Componentes=-vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv//
	
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
						Main.primaryStage.setHeight(489);
						Main.primaryStage.setWidth(872);
						Main.primaryStage.centerOnScreen();
						
						btFecharAbaChat.setVisible(true);
						btFecharAbaChat.setText("<");
						
						tabUsuario.setText(tvUsuariosAtivos.getSelectionModel().getSelectedItem().getValue());	
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});
		}
	}
	
	public void fechaAbaDoChat() {
		btFecharAbaChat.setVisible(false);
		btFecharAbaChat.setText(">");
		
		Main.primaryStage.setHeight(489);
		Main.primaryStage.setWidth(245);
		Main.primaryStage.centerOnScreen();
	}
	
	//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^-=Ações de Componentes=-^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^//


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		lbUsuarioAtivo.setText(user);
		
		ServerHandler sHandler = new ServerHandler(Main.conexao.getConnection(), Connection.entrada);
		Thread t = new Thread(sHandler);
		t.start();
	}

	
	//vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv-=Thread=-vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv//
	
	class ServerHandler implements Runnable {

		Socket cliente;
		ObjectInputStream entrada;

		public ServerHandler(Socket cliente, ObjectInputStream entrada) {
			this.cliente = cliente;
			this.entrada = entrada;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			try {
				while (true) {
					Object recebido = entrada.readObject();
					if (recebido instanceof Vector<?>) {
						for (String cliente : usuariosAtivos) {
							if(!((Vector<String>) recebido).contains(cliente)) {
								AlertUtils.showNotficacaoLogin(false, cliente);
							}
						}
						ViewCentralController.usuariosAtivos = (Vector<String>) recebido;
						carregaTreeView();
						if(!ViewCentralController.usuariosAtivos.lastElement().equals(user)) {
							AlertUtils.showNotficacaoLogin(true, ViewCentralController.usuariosAtivos.lastElement());					
						}		
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
