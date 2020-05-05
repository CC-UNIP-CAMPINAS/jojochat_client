package gui.controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Polyline;
import model.entities.Mensagem;
import utils.AlertUtils;
import utils.ConversorDataUtils;
import utils.FileUtils;

public class ViewBalaoArquivoDestinatarioController implements Initializable {

	private Mensagem mensagem;

	@FXML
    private AnchorPane apCentral;

    @FXML
    private Polyline triBalao1;

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
    private JFXSpinner spinCarregando;

    @FXML
    private JFXButton btDownload;

	@FXML
	void fazDownload() {

	}

	public void setaInformacoesIniciais(Mensagem mensagem) {
		this.mensagem = mensagem;
		lbNomeArquivo.setText(mensagem.getArquivo().getLocalizacaoServidor().getName());
		lbTamanhoArquivo.setText(FileUtils.conversorDeUnidade(mensagem.getArquivo().getLocalizacaoServidor()));
		lbMensagem.setText(mensagem.getMensagem());
		lbHorario.setText(ConversorDataUtils.getTimeToString(mensagem.getDateTime()));
	}
	
	public void setaInformacoesNovas() {
		lbTamanhoArquivo.setText(FileUtils.conversorDeUnidade(mensagem.getArquivo().getLocalizacaoDestinatario()));
	}
	
	public boolean verificaArquivo() {
		if(FileUtils.verificaArquivo(this.mensagem.getArquivo().getLocalizacaoDestinatario())) {
			btDownload.setVisible(false);
			spinCarregando.setVisible(false);
			lbTamanhoArquivo.setVisible(true);
			return true;
		}
		else {
			String caminho = FileUtils.getCaminhoArquivos();
			File listaArquivos = new File(caminho + File.separatorChar+String.valueOf(mensagem.getRemetente().getId() + ".txt"));
			if(!listaArquivos.exists()) {
				btDownload.setVisible(true);
				spinCarregando.setVisible(false);
				lbTamanhoArquivo.setVisible(false);
				return false;
			}
			else {
				mensagem.getArquivo().setLocalizacaoDestinatario(FileUtils.percorreListaArquivos(listaArquivos, mensagem.getArquivo().getLocalizacaoServidor().getName()));
				if(mensagem.getArquivo().getLocalizacaoDestinatario() != null && mensagem.getArquivo().getLocalizacaoDestinatario().exists()) {
					setaInformacoesNovas();
					btDownload.setVisible(false);
					spinCarregando.setVisible(false);
					lbTamanhoArquivo.setVisible(true);
					return true;
				}
				else {
					btDownload.setVisible(true);
					spinCarregando.setVisible(false);
					lbTamanhoArquivo.setVisible(false);
					return false;
				}
			}
		}
	}

	public void abreArquivo() {
		if(verificaArquivo()) {
			if (Desktop.isDesktopSupported()) {
				new Thread(() -> {
					try {
						Desktop.getDesktop().open(mensagem.getArquivo().getLocalizacaoDestinatario());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}).start();
			}
		}
		else {
			AlertUtils.showNotificacaoErroArquivoFaltante(mensagem.getArquivo().getLocalizacaoServidor());
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}
}