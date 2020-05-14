package gui.controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Polyline;
import model.entities.Colecao;
import model.entities.Mensagem;
import utils.AlertUtils;
import utils.ConnectionUtils;
import utils.ConversorDataUtils;
import utils.FileUtils;

public class ViewBalaoArquivoRemetenteController implements Initializable {

	private Mensagem mensagem;
	boolean parada;

	@FXML
    private AnchorPane apCentral;

    @FXML
    private Polyline triBalao;

    @FXML
    private AnchorPane apInterno;

    @FXML
    private AnchorPane apArquivo;

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
		Vector<Object> requisicao = new Vector<>();
		
		requisicao.add("arquivo");
		requisicao.add(mensagem);

		try {
			ConnectionUtils.saida.writeObject(requisicao);
			ConnectionUtils.saida.reset();
			parada = true;
			spinCarregando.setVisible(true);
			
			Task<Void> tarefa = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					while (parada) {
						Thread.sleep(0);
					}
					Platform.runLater(() -> {
						verificaArquivo();
					});
					return null;
				}
			};

			Platform.runLater(() -> {
				Thread t = new Thread(tarefa);
				t.start();
			});
			
			Task<Void> tarefaDownload = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					Thread.sleep(1000);
					@SuppressWarnings("unchecked")
					Vector<Object> requisicao = (Vector<Object>) Colecao.filaRequisicoes.get(0);
					Colecao.filaRequisicoes.clear();
					if(requisicao.get(0).equals("arquivo")) {
						Mensagem mensagemComArquivo = (Mensagem) requisicao.get(1);
						String destino = FileUtils.gravaArquivo(mensagemComArquivo.getArquivo(), FileUtils.getCaminhoArquivos()+File.separator+String.valueOf(ViewCentralController.getUserParaConversar().getId()));
						FileUtils.escreveListaArquivos(new File(FileUtils.getCaminhoArquivos()+File.separator+String.valueOf(ViewCentralController.getUserParaConversar().getId()+".txt")), "remetente;"+mensagemComArquivo.getArquivo().getLocalizacaoServidor().getName()+";"+destino+";");
					}
					parada = false;
					return null;
				}
			};

			Platform.runLater(() -> {
				Thread t = new Thread(tarefaDownload);
				t.start();
			});
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setaInformacoesNovas() {
		lbTamanhoArquivo.setText(FileUtils.conversorDeUnidade(mensagem.getArquivo().getLocalizacaoRemetente()));
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
		if(FileUtils.verificaArquivo(this.mensagem.getArquivo().getLocalizacaoRemetente())) {
			btDownload.setVisible(false);
			spinCarregando.setVisible(false);
			lbTamanhoArquivo.setVisible(true);
			return true;
		}
		else {
			String caminho = FileUtils.getCaminhoArquivos();
			File listaArquivos = new File(caminho + File.separatorChar+String.valueOf(mensagem.getDestinatario().getId() + ".txt"));
			if(!listaArquivos.exists()) {
				btDownload.setVisible(true);
				spinCarregando.setVisible(false);
				lbTamanhoArquivo.setVisible(false);
				return false;
			}
			else {
				mensagem.getArquivo().setLocalizacaoRemetente(FileUtils.percorreListaArquivos(listaArquivos, mensagem.getArquivo().getLocalizacaoServidor().getName()));
				if(mensagem.getArquivo().getLocalizacaoRemetente() != null && mensagem.getArquivo().getLocalizacaoRemetente().exists()) {
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
						Desktop.getDesktop().open(mensagem.getArquivo().getLocalizacaoRemetente());
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