package app;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    private static Stage stageLogin = new Stage();
    private static Scene login;

    @Override
    public void start(Stage arg0) throws Exception {
        try {
            FXMLLoader splashLoader = new FXMLLoader(getClass().getResource("/gui/views/SplashScreen.fxml"));
            Parent splashPane = splashLoader.load();

            // Cria a Janela do Splash
            // Define como transparente para que não apareça decoração de janela (maximizar,
            // minimizar)
            Stage splashStage = new Stage(StageStyle.TRANSPARENT);
            final Scene scene = new Scene(splashPane);
            scene.setFill(Color.TRANSPARENT); // Define que a cor do painel root seja transparente para que dê o efeito
                                              // de sombra
            splashStage.setScene(scene);

            // Cria o serviço para rodar alguma tarefa em background enquanto o splash é
            // mostrado (no caso somente um delay de 10s)
            Service<Boolean> splashService = new Service<Boolean>() {

                // Mostra o splash quando o serviço for iniciado
                @Override
                public void start() {
                    super.start();
                    splashStage.show(); // mostra a janela
                }

                // Simulação de uma tarefa pesada
                @Override
                protected Task<Boolean> createTask() {
                    return new Task<Boolean>() {
                        @Override
                        protected Boolean call() throws Exception {
                           /* Socket cliente = new Socket("127.0.0.1", 12345);
                            System.out.println("O cliente se conectou ao servidor!");

                            Scanner teclado = new Scanner(System.in);
                            PrintStream saida = new PrintStream(cliente.getOutputStream(), true, "UTF-8");

                            while (teclado.hasNextLine()) {
                                saida.println(teclado.nextLine());
                            }

                            saida.close();
                            teclado.close();
                            cliente.close();*/
    
                            return true;
                        }
                    };
                }

                // Quando a tarefa for finalizada fecha o splash e mostra a tela principal
                @Override
                protected void succeeded() {
                    splashStage.close(); // Fecha o splash
                    try {
                        Parent fxmlLogin = FXMLLoader.load(getClass().getResource("/gui/views/ViewChat.fxml"));
                        login = new Scene(fxmlLogin);
                        stageLogin.setScene(login);
                        stageLogin.show();

                    } catch (Exception ex) {
                    }
                }
            };

            splashService.start();

        } catch (

        IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);

    }
}