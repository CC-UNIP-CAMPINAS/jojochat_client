package gui.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import model.entities.Colecao;
import model.entities.Conversa;
import model.entities.Mensagem;
import model.entities.Usuario;
import utils.ConnectionUtils;
import utils.FileUtils;

public class ViewUserChatController extends Node implements Initializable {

	public Usuario usuario;
	public Image imgProfile;
	public Conversa conversa;
	private int numeroMensagens = 0;

	@FXML
	private AnchorPane apUserChat;

	@FXML
	private Circle circuloImagemPerfil;

	@FXML
	private Label lbUser;

	@FXML
	private Label lbMensagem;

	@FXML
	private Circle circuloNumeroMensagens;

	@FXML
	private Label lbNumeroMensagens;

	public void setaUsuario(Usuario usuario) {
		this.usuario = usuario;
		lbUser.setText(usuario.getNomeDeExibicao());
		setaImagemPerfil();
	}

	public void setaConversa(Conversa conversa) {
		this.conversa = conversa;
	}

	public void setaMensagem(Mensagem mensagem) {
		if (mensagem.getArquivo() != null) {
			lbMensagem.setText("Arquivo");
		} else {
			lbMensagem.setText(mensagem.getMensagem());
		}
	}

	public String pegaNome() {
		return lbUser.getText();
	}

	public void setaImagemPerfil() {
		Task<Void> tarefa = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				File arquivoImagem = new File(FileUtils.getCaminhoImagensPerfil() + File.separator + usuario.getId());
				if (!arquivoImagem.exists()) {
					arquivoImagem = new File(FileUtils.gravaImagemPerfil(usuario.getProfileImage(),
							String.valueOf(usuario.getId()), FileUtils.getCaminhoImagensPerfil()));
				}

				imgProfile = new Image(arquivoImagem.toURI().toString());
				Platform.runLater(() -> {
					circuloImagemPerfil.setFill(new ImagePattern(imgProfile));
				});
				return null;
			}
		};

		Platform.runLater(() -> {
			Thread t = new Thread(tarefa);
			t.start();
		});
	}

	public void achaChat(ViewUserChatController viewUserChatController) {
		for (ViewUserChatController ap : Colecao.chatsAtivos) {
			if (ap.equals(viewUserChatController)) {
				ap.apUserChat.setStyle("-fx-background-color: #A1A1A1");
			} else {
				ap.apUserChat.setStyle(null);
			}
		}
		for (ViewUserChatController ap : Colecao.chatsNaoAtivos) {
			if (ap.equals(viewUserChatController)) {
				ap.apUserChat.setStyle("-fx-background-color: #A1A1A1");
			} else {
				ap.apUserChat.setStyle(null);
			}
		}
	}

	public void mudaTabPane() {
		if (!ViewCentralController.tabPaneConversasStatic.getSelectionModel().getSelectedItem().getId()
				.equals("tabConversasSalvas")) {
			ViewCentralController.tabPaneConversasStatic.getSelectionModel().selectFirst();
		}
	}

	public void clicado() {
		achaChat(this);
		mudaTabPane();

		Colecao.associaConversaComChat();
		ViewCentralController.setUserParaConversar(usuario, conversa);
		ViewCentralController.circleImgCoversaStatic.setFill(new ImagePattern(imgProfile));
		requisitaHistoricoConversa();
	}

	public void requisitaHistoricoConversa() {
		Vector<Object> requisicao = new Vector<>();

		requisicao.add("historico");
		requisicao.add(ViewCentralController.getUser());
		requisicao.add(ViewCentralController.getUserParaConversar());

		try {
			ConnectionUtils.saida.writeObject(requisicao);
			ConnectionUtils.saida.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ViewUserChatController other = (ViewUserChatController) obj;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}

	public void aumentaNumeroMensagens() {
		numeroMensagens = Integer.parseInt(lbNumeroMensagens.getText());
		numeroMensagens++;
		lbNumeroMensagens.setVisible(true);
		circuloNumeroMensagens.setVisible(true);
		lbNumeroMensagens.setText("" + numeroMensagens);

	}

	public void zeraNumeroMensagens() {
		lbNumeroMensagens.setText("0");
		lbNumeroMensagens.setVisible(false);
		circuloNumeroMensagens.setVisible(false);

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	@Override
	protected boolean impl_computeContains(double arg0, double arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BaseBounds impl_computeGeomBounds(BaseBounds arg0, BaseTransform arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected NGNode impl_createPeer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object impl_processMXNode(MXNodeAlgorithm arg0, MXNodeAlgorithmContext arg1) {
		// TODO Auto-generated method stub
		return null;
	}
}