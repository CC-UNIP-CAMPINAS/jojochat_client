package utils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import org.controlsfx.control.Notifications;

import gui.controllers.ViewInformacaoArquivoController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.entities.Mensagem;

public class AlertUtils {

	
	public static void showNotificacaoLogin(boolean status, String usuario) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if(status) {
					Notifications.create().title("Novo Login!")
			        .text(usuario + " se logou!")
			        .showInformation();
				}
				else {
					Notifications.create().title("Usuário Saiu!!")
			        .text(usuario + " saiu!")
			        .showInformation();
				}
			}
		});
	}
	
	public static void showNotificacaoErroLogin(int quant) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if(!(quant == 2)) {
					Notifications not = Notifications.create();
					not.position(Pos.CENTER);
					not.title("Dados incorretos!");
					not.text("Usuário ou senha não batem com nenhuma entrada no banco de dados.");
					not.showWarning();
					
				}
				else {
					Notifications not = Notifications.create();
					not.position(Pos.CENTER);
					not.title("Usuário já logado!");
					not.text("Esse usuários está logado em outro cliente!");
					not.showWarning();
				}
			}
		});
	}
	
	public static void showNotificacaoCadastro(int i) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {		
				if(i == 1) {
					Notifications not = Notifications.create();
					not.position(Pos.CENTER);
					not.title("Dados incorretos!");
					not.text("Preencha todos os campos!");
					not.showWarning();
				}
				if (i == 2){
					Notifications not = Notifications.create();
					not.position(Pos.CENTER);
					not.title("Dados duplicados");
					not.text("Usuário já registrado na base de dados");
					not.showWarning();
				}
				if (i == 3){
					Notifications not = Notifications.create();
					not.position(Pos.CENTER);
					not.title("Cadastro Realizado com sucesso!");
					not.text("Utilize seus dados para login");
					not.showConfirm();
				}
			}
		});
	}
	
	public static void showNotificacaoErroArquivoFaltante(File arquivo) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Notifications not = Notifications.create();
				not.position(Pos.TOP_RIGHT);
				not.title("Arquivo não encontrado!");
				not.text("O arquivo: "+arquivo.getName()+" não foi encontrado! Solicite o download!");
				not.showWarning();
			}
		});
	}
	
	public static void showNotificacaoNovaMensagem(Mensagem mensagem) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
					Notifications not = Notifications.create();
					not.position(Pos.BOTTOM_RIGHT);
					not.title("Nova Mensagem!");
					not.text(mensagem.getRemetente().getNomeDeExibicao() + ": " + mensagem.getMensagem());
					not.showInformation();				
			}
		});
	}
	
	public static void showNotificacaoArquivoGrande(File arquivo) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
					Notifications not = Notifications.create();
					not.position(Pos.CENTER);
					not.title("Atenção!");
					not.text(arquivo.getName() + " é maior que 8MB, selecione um novo arquivo.");
					not.showWarning();				
			}
		});
	}
	
	public String showJanelaConfirmacaoEnvio(File arquivo, LocalDateTime horario) {
		try {
			FXMLLoader loaderArquivo = new FXMLLoader(getClass().getResource("/gui/views/ViewInformacaoArquivo.fxml"));
			Parent fxmlArquivo = (Parent) loaderArquivo.load();
			ViewInformacaoArquivoController controlador = loaderArquivo.getController();
			controlador.setInformacoes(arquivo);
			
			Scene sceneCentral = new Scene(fxmlArquivo);
			sceneCentral.setFill(Color.TRANSPARENT);
			Stage stageArquivo = new Stage(StageStyle.TRANSPARENT);
			stageArquivo.setScene(sceneCentral);
			stageArquivo.initModality (Modality.APPLICATION_MODAL);
			
			controlador.setStage(stageArquivo);
			if(FileUtils.isImg(arquivo)) {
				controlador.setImagemDeFundo(arquivo);
			}
			else {
				stageArquivo.setWidth(642.0);
				stageArquivo.setHeight(190.0);
			}
			
			stageArquivo.setResizable(false);
			
			stageArquivo.showAndWait();
			
			return controlador.retornaMensagem();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
