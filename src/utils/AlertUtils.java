package utils;

import org.controlsfx.control.Notifications;

import javafx.application.Platform;
import javafx.geometry.Pos;
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
	public static void showNotificacaoErroMensagem(Mensagem mensagem) {
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
	
}
