package gui.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.FileUtils;

public class ViewInformacaoArquivoController implements Initializable {

	private Stage stage;
	private File arquivo;
	
	@FXML
	private AnchorPane acPrincipal;

	@FXML
	private TextField tfComentario;

	@FXML
	private Label lbComentario;

	@FXML
	private AnchorPane acCentral;

	@FXML
	private ImageView imgPreview;

	@FXML
	private AnchorPane acInterno;

	@FXML
	private Label lbTamanhoArquivo;

	@FXML
	private ImageView imgArquivo;

	@FXML
	private Label lbNomeArquivo;

	@FXML
	private JFXButton btEnviar;

	@FXML
	private JFXButton btCancelar;
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setInformacoes(File arquivo) {
		lbNomeArquivo.setText(arquivo.getName());
		lbTamanhoArquivo.setText(FileUtils.conversorDeUnidade(arquivo));
		this.arquivo = arquivo;
	}

	public void setImagemDeFundo(File arquivo){
		Image image = new Image(arquivo.toURI().toString());
		imgPreview.setImage(image);
	}
	
	public void acaoBtCancelar() {
		this.stage.close();
		ViewCentralController.setArquivoParaEnvio(null);
		ViewCentralController.paneOpacoStatic.setVisible(false);
	}
	
	public void acaoBtEnviar() {
		this.stage.close();
		ViewCentralController.paneOpacoStatic.setVisible(false);
		try {
			gravaArquivo();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String retornaMensagem() {
		return tfComentario.getText();
	}
	
	public void gravaArquivo() throws IOException {
		String caminho = FileUtils.getCaminhoArquivos()+File.separatorChar+String.valueOf(ViewCentralController.getUserParaConversar().getId());
		FileUtils.copiaArquivo(arquivo, caminho);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ViewCentralController.paneOpacoStatic.setVisible(true);

	}

}
