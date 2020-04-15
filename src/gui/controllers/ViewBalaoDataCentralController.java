package gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ViewBalaoDataCentralController implements Initializable{

	@FXML
    private AnchorPane apCentral;

    @FXML
    private AnchorPane apInterno;

    @FXML
    private Label lbData;

	
    public void setaData(String data) {
    	lbData.setText(data);
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
}
