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
import utils.IdentificadorSoUtils;

public class ViewBalaoArquivoDestinatarioController implements Initializable {

	private File arquivo;

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
	private ImageView imgDownload;

	@FXML
	private JFXSpinner spinCarregando;

	public File getArquivo() {
		return arquivo;
	}

	public void setArquivo(File arquivo) {
		this.arquivo = arquivo;
	}

	@FXML
	void fazDownload() {

	}

	public void setaInformacoes(Mensagem mensagem) {
		this.arquivo = mensagem.getArquivo().getLocalizacao();
		lbNomeArquivo.setText(arquivo.getName());
		lbTamanhoArquivo.setText(FileUtils.conversorDeUnidade(arquivo));
		lbMensagem.setText(mensagem.getMensagem());
		lbHorario.setText(ConversorDataUtils.getTimeToString(mensagem.getDateTime()));
		
		verificaArquivo(mensagem);
	}
	
	public void verificaArquivo(Mensagem mensagem) {
		if(FileUtils.verificaArquivo(arquivo)) {
			spinCarregando.setVisible(false);
			imgDownload.setVisible(false);
		}
		else {
			String caminho = System.getProperty("user.home")+File.separatorChar+"Documents"+File.separatorChar+"JOJO_DATA"+ File.separatorChar+"Arquivos";
			if (IdentificadorSoUtils.sistema().equals("linux")){
					caminho = System.getProperty("user.home")+File.separatorChar+"Documents"+File.separatorChar+"JOJO_DATA"+ File.separatorChar+"Arquivos";
			}
			caminho += File.separatorChar+String.valueOf(mensagem.getDestinatario().getId());
			mensagem.getArquivo().setLocalizacao(FileUtils.procuraArquivo(caminho, arquivo));
			if(!mensagem.getArquivo().getLocalizacao().exists()) {
				imgDownload.setVisible(true);
				AlertUtils.showNotificacaoErroArquivoFaltante(arquivo);
			}
			else {
				setaInformacoes(mensagem);
			}
		}
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