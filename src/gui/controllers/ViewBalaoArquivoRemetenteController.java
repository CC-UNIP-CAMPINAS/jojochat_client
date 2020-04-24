package gui.controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXSpinner;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Polyline;
import model.entities.Mensagem;
import utils.AlertUtils;
import utils.ConversorDataUtils;
import utils.FileUtils;

public class ViewBalaoArquivoRemetenteController implements Initializable {

	private File arquivo;

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

	public void setaInformacoes(Mensagem mensagem) {
		this.arquivo = mensagem.getArquivo().getLocalizacao();
		lbNomeArquivo.setText(arquivo.getName());
		lbTamanhoArquivo.setText(FileUtils.conversorDeUnidade(arquivo));
		lbMensagem.setText(mensagem.getMensagem());
		lbHorario.setText(ConversorDataUtils.getTimeToString(mensagem.getDateTime()));
		
		verificaArquivo();
	}
	
	public boolean verificaArquivo() {
		if(FileUtils.verificaArquivo(arquivo)) {
			spinCarregando.setVisible(false);
			imgDownload.setVisible(false);
			return true;
		}
		else {
			imgDownload.setVisible(true);
			AlertUtils.showNotificacaoErroArquivoFaltante(arquivo);
			return false;
		}
	}

	public void abreArquivo() {
		if(verificaArquivo()) {
			if (Desktop.isDesktopSupported()) {
				new Thread(() -> {
					try {
						Desktop.getDesktop().open(arquivo);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}).start();
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}
}