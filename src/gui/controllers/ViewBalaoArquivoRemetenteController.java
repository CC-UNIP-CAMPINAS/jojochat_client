package gui.controllers;

import java.awt.Desktop;
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
import utils.ConversorDataUtils;
import utils.FileUtils;

public class ViewBalaoArquivoRemetenteController implements Initializable {

	private Mensagem mensagem;

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
		this.mensagem = mensagem;
		lbNomeArquivo.setText(mensagem.getArquivo().getLocalizacaoRemetente().getName());
		lbTamanhoArquivo.setText(FileUtils.conversorDeUnidade(mensagem.getArquivo().getLocalizacaoRemetente()));
		lbMensagem.setText(mensagem.getMensagem());
		lbHorario.setText(ConversorDataUtils.getTimeToString(mensagem.getDateTime()));

		verificaArquivo();
	}

	public boolean verificaArquivo() {
		if (FileUtils.verificaArquivo(mensagem.getArquivo().getLocalizacaoRemetente())) {
			spinCarregando.setVisible(false);
			imgDownload.setVisible(false);
			return true;
		} else {
			spinCarregando.setVisible(false);
			imgDownload.setVisible(true);
			return false;// TENHO QUE SOLICITAR O ARQUIVO;
		}

	}

	public void abreArquivo() {
		if (verificaArquivo()) {
			if (Desktop.isDesktopSupported()) {
				new Thread(() -> {
					try {
						Desktop.getDesktop().open(mensagem.getArquivo().getLocalizacaoRemetente());
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