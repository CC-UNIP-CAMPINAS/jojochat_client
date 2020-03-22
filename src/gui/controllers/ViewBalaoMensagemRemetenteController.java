package gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Polyline;

public class ViewBalaoMensagemRemetenteController implements Initializable{

	@FXML
    private AnchorPane apCentral;

    @FXML
    private Polyline triBalao;

    @FXML
    private AnchorPane apInterno;

    @FXML
    private Label lbMensagem;

    @FXML
    private Label lbHorario;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

}
