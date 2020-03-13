package gui.controllers;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ViewChatController implements Initializable {

    Socket cliente = null;
    PrintStream saida = null;

    @FXML
    public TextArea taEscritura;

    @FXML
    public TextArea taMensagens;

    @FXML
    public Button btEnviar;

    @FXML
    public void enviaMensagem() {
        String mensagem = taEscritura.getText();
        saida.println(LocalDate.now() + " - " + mensagem);
        gravaMensagemTextArea(LocalDate.now() + " - " + mensagem);
        taEscritura.clear();
    }

    public void gravaMensagemTextArea(String mensagem){
        String historico = taMensagens.getText();
        historico += mensagem + "\n\n";
        taMensagens.setText(historico);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            cliente = new Socket("127.0.0.1", 12345);
            saida = new PrintStream(cliente.getOutputStream(), true, "UTF-8");
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}