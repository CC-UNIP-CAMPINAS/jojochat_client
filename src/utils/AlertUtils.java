package utils;

import org.controlsfx.control.Notifications;

import javafx.application.Platform;

public class AlertUtils {

	
	public static void showNotficacaoLogin(boolean status, String usuario) {
		
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if(status) {
					Notifications.create().title("Novo Login!")
			        .text(usuario + " Se logou!")
			        .showInformation();
				}
				else {
					Notifications.create().title("Usuário Saiu!!")
			        .text(usuario + " Saiu!")
			        .showInformation();
				}
			}
		});
	}
	
}
