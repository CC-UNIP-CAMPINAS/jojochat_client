package gui.controllers;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.Vector;

import app.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.entities.Mensagem;
import model.entities.Usuario;
import utils.AlertUtils;
import utils.ConnectionUtils;
import utils.ConversorDataUtils;

public class ViewCentralController implements Initializable {
	Socket conexao;
	private static Usuario user;
	private static Usuario userParaConversar;
	public static AnchorPane apCentralStatic;

	static Vector<Usuario> usuariosAtivos = new Vector<>();

	@FXML
	private ScrollPane scrollPaneUser;

	@FXML
	private VBox vbUsuariosLogadas;

	@FXML
	private AnchorPane apCentral;

	@FXML
	private AnchorPane apCentralInf;

	@FXML
	private ImageView btArquivo;

	@FXML
	private ImageView btEnviar;

	@FXML
	private ImageView btEmoji;

	@FXML
	private TextArea taEscritura;

	@FXML
	private AnchorPane apCentralSup;

	@FXML
	private Label lbUserChamado;

	@FXML
	private ImageView imgUserChamado;

	@FXML
	private ScrollPane scrollPaneMensagens;

	@FXML
	private VBox vbMensagem;

	@FXML
	private AnchorPane apLeftSup;

	@FXML
	private Label lbUser;

	@FXML
	private ImageView imgUser;

	@FXML
	private TextField tfPesquisa;

	// #################Gets/Sets################# //

	public static Usuario getUser() {
		return user;
	}

	public static void setUser(Usuario user) {
		ViewCentralController.user = user;
	}

	public static Usuario getUserParaConversar() {
		return userParaConversar;
	}

	public static void setUserParaConversar(Usuario userParaConversar) {
		ViewCentralController.userParaConversar = userParaConversar;
		ViewCentralController.apCentralStatic.setVisible(true);
		
	}

	// #################Associação de componentes Statics################# //
	
	public void associaComponentesStaticos() {
		ViewCentralController.apCentralStatic = apCentral;
	}
	
	// #################Ações de Componentes################# //

	public void abreConversa() {
		lbUserChamado.setText(ViewCentralController.userParaConversar.getNomeDeExibicao());
	}

	public void carregaVboxUsuariosLogados() throws IOException {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				vbUsuariosLogadas.getChildren().clear();
				for (Usuario usuarios : usuariosAtivos) {
					if (!usuarios.equals(user)) {
						FXMLLoader userChatLoader;
						try {
							userChatLoader = new FXMLLoader(getClass().getResource("/gui/views/ViewUserChat.fxml"));
							Parent userChatParent = (Parent) userChatLoader.load();
							ViewUserChatController controlador = userChatLoader.getController();
							controlador.setaUsuario(usuarios);
							vbUsuariosLogadas.getChildren().add(userChatParent);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
	}

	public void enviaMensagem() {
		LocalDateTime horario = LocalDateTime.now();
		String textoDaMensagem = taEscritura.getText();
		taEscritura.clear();

		Vector<Object> requisicao = new Vector<>();
		Mensagem mensagemParaEnvio = new Mensagem(textoDaMensagem, user, ViewCentralController.getUserParaConversar(), horario);
		
		requisicao.add("mensagem");	
		requisicao.add(mensagemParaEnvio);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				vbMensagem.getChildren().add(criaBalaoDeMensagem(mensagemParaEnvio, 2));
			}
		});

		try {
			ConnectionUtils.saida.writeObject(requisicao);
			ConnectionUtils.saida.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void recebeMensagem(Vector<?> requisicao) {
		Mensagem mensagem = (Mensagem) requisicao.get(1);
		if(mensagem.getRemetente().equals(userParaConversar)) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					vbMensagem.getChildren().add(criaBalaoDeMensagem(mensagem, 1));
				}
			});
		}
		else {
			AlertUtils.showNotificacaoErroMensagem(mensagem);
		}		
	}

	@SuppressWarnings("unchecked")
	public void recebeBroadcast(Vector<?> requisicao) throws IOException {
		for (Usuario usuario : usuariosAtivos) {
			if (!((Vector<Usuario>) requisicao.get(1)).contains(usuario)) {
				AlertUtils.showNotificacaoLogin(false, usuario.getUsuario());
			}
		}
		ViewCentralController.usuariosAtivos = (Vector<Usuario>) requisicao.get(1);
		carregaVboxUsuariosLogados();
		if (!ViewCentralController.usuariosAtivos.lastElement().equals(user)) {
			AlertUtils.showNotificacaoLogin(true, ViewCentralController.usuariosAtivos.lastElement().getUsuario());
		}
	}

	public HBox criaBalaoDeMensagem(Mensagem conversa, int opcao) {
		String horarioFormatado = ConversorDataUtils.getTimeToString(conversa.getDateTime());
		
		if (opcao == 1) {
			FXMLLoader balaoDestinatario;
			try {
				balaoDestinatario = new FXMLLoader(getClass().getResource("/gui/views/ViewBalaoMensagemDestinatario.fxml"));
				Parent parentBalaoDestinatario = (Parent) balaoDestinatario.load();
				ViewBalaoMensagemDestinatarioController controlador = balaoDestinatario.getController();
				controlador.setaMensagem(conversa.getMensagem(), horarioFormatado);

				HBox hboxMensagem = new HBox();
				hboxMensagem.setPrefHeight(HBox.USE_COMPUTED_SIZE);
				hboxMensagem.setPrefWidth(HBox.USE_COMPUTED_SIZE);
				hboxMensagem.setMaxSize(HBox.USE_COMPUTED_SIZE, HBox.USE_COMPUTED_SIZE);
				hboxMensagem.setMinSize(HBox.USE_COMPUTED_SIZE, HBox.USE_COMPUTED_SIZE);
				HBox.setMargin(hboxMensagem, new Insets(0, 0, 0, 10));
				hboxMensagem.setAlignment(Pos.CENTER_LEFT);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						hboxMensagem.getChildren().add(parentBalaoDestinatario);
					}
				});
				return hboxMensagem;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			FXMLLoader balaoRemetente;
			try {
				balaoRemetente = new FXMLLoader(getClass().getResource("/gui/views/ViewBalaoMensagemRemetente.fxml"));
				Parent parentBalaoRemetente = (Parent) balaoRemetente.load();
				ViewBalaoMensagemRemetenteController controlador = balaoRemetente.getController();
				controlador.setaMensagem(conversa.getMensagem(), horarioFormatado);

				HBox hboxMensagem = new HBox();
				hboxMensagem.setPrefHeight(HBox.USE_COMPUTED_SIZE);
				hboxMensagem.setPrefWidth(HBox.USE_COMPUTED_SIZE);
				hboxMensagem.setMaxSize(HBox.USE_COMPUTED_SIZE, HBox.USE_COMPUTED_SIZE);
				hboxMensagem.setMinSize(HBox.USE_COMPUTED_SIZE, HBox.USE_COMPUTED_SIZE);
				HBox.setMargin(hboxMensagem, new Insets(0, 10, 0, 0));
				hboxMensagem.setAlignment(Pos.CENTER_RIGHT);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						hboxMensagem.getChildren().add(parentBalaoRemetente);
					}
				});
				return hboxMensagem;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		lbUser.setText(user.getNomeDeExibicao());
		
		associaComponentesStaticos();

		ServerHandler sHandler = new ServerHandler(Main.conexao.getConnection(), ConnectionUtils.entrada);
		Thread t = new Thread(sHandler);
		t.start();
	}

	// #################Thread#################//

	class ServerHandler implements Runnable {

		Socket cliente;
		ObjectInputStream entrada;
		String operacao;
		Vector<?> requisicao;
		Object recebido;

		public ServerHandler(Socket cliente, ObjectInputStream entrada) {
			this.cliente = cliente;
			this.entrada = entrada;
		}

		@Override
		public void run() {
			try {
				while (true) {
					recebido = entrada.readObject();
					if (recebido instanceof Vector<?>) {

						requisicao = (Vector<?>) recebido;
						operacao = (String) requisicao.get(0);

						switch (operacao) {
						case "broadcast":
							recebeBroadcast(requisicao);
							break;

						case "mensagem":
							recebeMensagem(requisicao);
							break;
						default:
							break;
						}
					}
				}
			}

			catch (EOFException | SocketException e) {
				e.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (ClassNotFoundException e) {
			} finally {
				try {
					cliente.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
