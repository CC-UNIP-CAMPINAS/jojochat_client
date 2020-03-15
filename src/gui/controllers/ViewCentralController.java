package gui.controllers;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import utils.Connection;

public class ViewCentralController implements Initializable {
	Socket conexao;

	ArrayList<String> usuariosAtivos = new ArrayList<>();

	//ObjectInputStream entrada = null;

	//ObjectOutputStream saida = null;

	@FXML
	private TreeView<String> tvUsuariosAtivos;

	@FXML
	public void carregaTreeView() throws ClassNotFoundException, IOException {
		TreeItem<String> treeItemPrincipal = new TreeItem<String>("Usuários Ativos");
		treeItemPrincipal.setExpanded(true);

		for (String usuarios : usuariosAtivos) {
			TreeItem<String> treeItemUsuario = new TreeItem<String>(usuarios);
			treeItemPrincipal.getChildren().add(treeItemUsuario);
		}

		tvUsuariosAtivos.setRoot(treeItemPrincipal);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			Connection conexao = new Connection("localhost", 12345);
			ObjectOutputStream saida = new ObjectOutputStream(conexao.getConnection().getOutputStream());
			ObjectInputStream entrada = new ObjectInputStream(conexao.getConnection().getInputStream());
			System.out.println(entrada.toString());
			ServerHandler cHandler = new ServerHandler(conexao.getConnection(), saida, entrada);
			Thread t = new Thread(cHandler);
			t.start();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class ServerHandler implements Runnable {

		Socket cliente;
		ObjectOutputStream saida;
		ObjectInputStream entrada;
		public ServerHandler(Socket cliente, ObjectOutputStream saida, ObjectInputStream entrada) {
			this.cliente = cliente;
			this.saida = saida;
			this.entrada = entrada;
		}

		@Override
		public void run() {
			try {
				while (true) {
					if (!cliente.isClosed()) {
						Object recebido = entrada.readObject();
						System.out.println(entrada.toString());
		
						usuariosAtivos = (ArrayList<String>) recebido;
						carregaTreeView();
						System.out.println("\n-----------\n");
						for (String string : usuariosAtivos) {
							System.out.println(string);
						}

					}
				}
			}

			catch (EOFException | SocketException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException ex) {
				System.out.println("Tipo de objeto não esperado");
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				try {
					entrada.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}