package gui.controllers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
	
	ObjectInputStream entrada = null;
	
	ObjectOutputStream saida = null;
	
	@FXML
	private TreeView<String> tvUsuariosAtivos;
	
	@FXML
	public void carregaTreeView() {
		try {
			usuariosAtivos = (ArrayList<String>) entrada.readObject();
			
			for (String socket : usuariosAtivos) {
				System.out.println(socket);
			}
			
			TreeItem<String> treeItemPrincipal = new TreeItem<String>("Usuários Ativos");
			treeItemPrincipal.setExpanded(true);

			for (String usuarios : usuariosAtivos) {
				TreeItem<String>  treeItemUsuario = new TreeItem<String>(usuarios);
				treeItemPrincipal.getChildren().add(treeItemUsuario);
				}

			tvUsuariosAtivos.setRoot(treeItemPrincipal);
			
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    	try {
    		Connection conexao = new Connection("localhost", 12345);
    		saida = new ObjectOutputStream(conexao.getConnection().getOutputStream());
			entrada = new ObjectInputStream(conexao.getConnection().getInputStream());
			carregaTreeView();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }  
}