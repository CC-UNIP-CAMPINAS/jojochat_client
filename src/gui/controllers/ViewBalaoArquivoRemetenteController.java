package gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXSpinner;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Polyline;

public class ViewBalaoArquivoRemetenteController implements Initializable{

    @FXML
    private AnchorPane apCentral;

    @FXML
    private Polyline triBalao;

    @FXML
    private AnchorPane apInterno;

    @FXML
    private Label lbTamanhoArquivo;

    @FXML
    private Label lbNomeArquivo;

    @FXML
    private Label lbMensagem;

    @FXML
    private Label lbHorario;

    @FXML
    private ImageView imgDownload;

    @FXML
    private JFXSpinner spinCarregando;

    @FXML
    void fazDownload() {

    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}