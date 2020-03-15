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

import app.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import utils.Connection;

public class ViewCentralController implements Initializable {
	Socket conexao;

	static Vector<String> usuariosAtivos = new Vector<String>();

	// ObjectInputStream entrada = null;

	// ObjectOutputStream saida = null;

	@FXML
	private TreeView<String> tvUsuariosAtivos;

	public void carregaTreeView() throws ClassNotFoundException, IOException, IllegalStateException {
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			Connection conexao = new Connection("localhost", 12345);
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
					ViewCentralController.usuariosAtivos = (Vector<String>) recebido;
					carregaTreeView();
				}
			}

			catch (EOFException | SocketException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException ex) {
				System.out.println("Tipo de objeto não esperado");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			finally {
				try {
					cliente.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
