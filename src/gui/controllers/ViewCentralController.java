package gui.controllers;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.Vector;

import app.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
	public Button btEnviar;

	@FXML
	public Button btFecharAbaChat;

	@FXML
	private VBox vbUsuariosLogadas;

	@FXML
	private Label lbUsuarioAtivo;

	// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv-=Gets/Sets=-vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv//

	public static String getUser() {
		return user;
	}

	public static void setUser(String user) {
		ViewCentralController.user = user;
	}

	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^-=Gets/Sets=-
	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^//

	// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv-=Ações de
	// Componentes=-vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv//

	public void carregaTreeView() throws IOException {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				vbUsuariosLogadas.getChildren().clear();
				for (String usuarios : usuariosAtivos) {
					if (!usuarios.equals(user)) {
						FXMLLoader userChatLoader;
						try {
							userChatLoader = new FXMLLoader(getClass().getResource("/gui/views/ViewUserChat.fxml"));
							Parent userChatParent = (Parent) userChatLoader.load();
							ViewUserChatController controlador = userChatLoader.getController();
							controlador.setaNomeUsuario(usuarios);
							vbUsuariosLogadas.getChildren().add(userChatParent);
						} catch (IOException e) {
							e.printStackTrace();
						}	
					}
				}
			}
		});
	}

	//public void selecionaChat() {
		//vbUsuariosLogadas.getChildren().get(1).
		
	//}

	public void fechaAbaDoChat() {
		btFecharAbaChat.setVisible(false);
		btFecharAbaChat.setText(">");

		Main.primaryStage.setHeight(489);
		Main.primaryStage.setWidth(245);
		Main.primaryStage.centerOnScreen();
	}

	public void enviaMensagem() {
		int hora = LocalDateTime.now().getHour();
		int minuto = LocalDateTime.now().getMinute();
		String mensagem = taMensagens.getText() + "(" + hora + ":" + minuto + ") - " + user + ": "
				+ taEscritura.getText() + "\n";
		taMensagens.setText(mensagem);
		taMensagens.end();
		taEscritura.clear();

		Vector<Object> teste = new Vector<>();
		teste.add("mensagem");
		teste.add(tabUsuario.getText());
		teste.add(user);
		teste.add(mensagem);

		try {
			Connection.saida.writeObject(teste);
			Connection.saida.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void recebeMensagem(Vector<?> requisicao) {
		String remetente = (String) requisicao.get(2);
		String mensagem = (String) requisicao.get(3);
		TreeItem<String> treeItemUsuario = new TreeItem<String>(remetente);
//		for (TreeItem<String> item : tvUsuariosAtivos.getRoot().getChildren()) {
//			if(item.getValue().equals(remetente)) {
//			}
//		}
		taMensagens.setText(mensagem);
		taMensagens.end();
	}

	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^-=Ações de
	// Componentes=-^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^//

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		lbUsuarioAtivo.setText(user);

		ServerHandler sHandler = new ServerHandler(Main.conexao.getConnection(), Connection.entrada);
		Thread t = new Thread(sHandler);
		t.start();
	}

	// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv-=Thread=-vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv//

	class ServerHandler implements Runnable {

		Socket cliente;
		ObjectInputStream entrada;
		String operacao;
		Vector<?> requisicao;
		Object recebido;
		String mensagem;

		public ServerHandler(Socket cliente, ObjectInputStream entrada) {
			this.cliente = cliente;
			this.entrada = entrada;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			try {
				while (true) {
					recebido = entrada.readObject();
					if (recebido instanceof Vector<?>) {
						requisicao = (Vector<?>) recebido;
						operacao = (String) requisicao.get(0);

						switch (operacao) {
						case "broadcast":

							for (String cliente : usuariosAtivos) {
								if (!((Vector<String>) requisicao.get(1)).contains(cliente)) {
									AlertUtils.showNotficacaoLogin(false, cliente);
								}
							}
							ViewCentralController.usuariosAtivos = (Vector<String>) requisicao.get(1);
							carregaTreeView();
							if (!ViewCentralController.usuariosAtivos.lastElement().equals(user)) {
								AlertUtils.showNotficacaoLogin(true,
										ViewCentralController.usuariosAtivos.lastElement());
							}

							break;

						case "mensagem":
							// mensagem = (String) requisicao.get(3);
							recebeMensagem(requisicao);
							break;
						default:
							break;
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
