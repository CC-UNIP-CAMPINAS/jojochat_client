package gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import app.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ViewLoginController implements Initializable{

	private static Scene sceneCentral;
	
	@FXML
	public Button btLogin;
	
	@FXML
	public TextField tfLogin;
	
	public void fazLogin() {
		Parent fxmlCentral;
		try {
			fxmlCentral = FXMLLoader.load(getClass().getResource("/gui/views/ViewCentral.fxml"));
			sceneCentral = new Scene(fxmlCentral);
			Main.primaryStage.setScene(sceneCentral);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}   
}