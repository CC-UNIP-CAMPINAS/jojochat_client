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
import model.entities.Colecao;
import model.entities.Conversa;
import model.entities.Mensagem;
import model.entities.Usuario;
import utils.AlertUtils;
import utils.ConnectionUtils;
import utils.ConversorDataUtils;

public class ViewCentralController implements Initializable {
	Socket conexao;
	private static Usuario user;
	private static Usuario userParaConversar;
	private static Conversa conversaAtual;
	public static AnchorPane apCentralStatic;

	
	@FXML
	private ScrollPane scrollPaneUser;

	@FXML
	private VBox vbUsuariosLogadas;
	
	@FXML
	private VBox vbConversas;

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

	public static void setUserParaConversar(Usuario userParaConversar, Conversa conversa) {
		ViewCentralController.userParaConversar = userParaConversar;
		ViewCentralController.conversaAtual = conversa;
		ViewCentralController.apCentralStatic.setVisible(true);
	}

	// #################Associação de componentes Statics################# //
	
	public void associaComponentesStaticos() {
		ViewCentralController.apCentralStatic = apCentral;
	}
	
	// #################Utilitarios do controlador################# //
	
	public void abreConversa() {
		lbUserChamado.setText(ViewCentralController.userParaConversar.getNomeDeExibicao());
	}
	
	public ViewUserChatController buscaUsuarioChat(Usuario usuario) {
		if(userParaConversar != null) {
			for (ViewUserChatController viewUserChatController : Colecao.chatsAtivos) {
				if(usuario.equals(viewUserChatController.usuario)) {
					return viewUserChatController;
				}
			}
		}	
		return null;
	}
	
	public void atualizaVboxConversa(Mensagem mensagem, Usuario usuario) {
		ViewUserChatController userChat = buscaUsuarioChat(usuario);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				userChat.setaMensagem(mensagem);
			}
		});
	}
	
	public void limpaConversa() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				vbMensagem.getChildren().clear();
			}
		});
	}
	
	public void colocaBalaoConversa(Mensagem conversa, int opcao) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if(opcao == 3) {
					vbMensagem.getChildren().add(criaBalaoDeData(conversa));
				}
				else {
					vbMensagem.getChildren().add(criaBalaoDeMensagem(conversa, opcao));
				}
			}
		});
	}
	
	public void carregaVboxUsuariosLogados() throws IOException {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				vbUsuariosLogadas.getChildren().clear();
				Colecao.chatsNaoAtivos.clear();
				for (Usuario usuarios : Colecao.usuariosAtivos) {
					if (!usuarios.equals(user)) {
						FXMLLoader userChatLoader;
						try {
							userChatLoader = new FXMLLoader(getClass().getResource("/gui/views/ViewUserChat.fxml"));
							Parent userChatParent = (Parent) userChatLoader.load();
							ViewUserChatController controlador = userChatLoader.getController();
							controlador.setaUsuario(usuarios);
							Colecao.chatsNaoAtivos.add(controlador);
							vbUsuariosLogadas.getChildren().add(userChatParent);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
	}
	
	public void carregaVboxConversas(Vector<Conversa> conversas) throws IOException {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				vbConversas.getChildren().clear();
				Colecao.chatsAtivos.clear();
				for (Conversa conversa : conversas) {
					FXMLLoader userChatLoader;
					try {
						userChatLoader = new FXMLLoader(getClass().getResource("/gui/views/ViewUserChat.fxml"));
						Parent userChatParent = (Parent) userChatLoader.load();
						
						ViewUserChatController controlador = userChatLoader.getController();
						controlador.setaUsuario(conversa.getParticipante2());
						controlador.setaMensagem(conversa.getMensagem());
						controlador.setaConversa(conversa);
						
						Colecao.chatsAtivos.add(controlador);
						vbConversas.getChildren().add(userChatParent);
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				Colecao.associaConversaComChat();
				ViewUserChatController userChat = buscaUsuarioChat(userParaConversar);
				if(userChat != null && userParaConversar != null) {
					conversaAtual = userChat.conversa;
				}								
			}
		});
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
	
	public HBox criaBalaoDeData(Mensagem conversa) {
		String data = ConversorDataUtils.getDateToString(conversa.getDateTime());
			FXMLLoader balaoData;
			try {
				balaoData = new FXMLLoader(getClass().getResource("/gui/views/ViewBalaoDataCentral.fxml"));
				Parent parentBalaoData = (Parent) balaoData.load();
				ViewBalaoDataCentralController controlador = balaoData.getController();
				controlador.setaData(data);

				HBox hboxMensagem = new HBox();
				hboxMensagem.setPrefHeight(HBox.USE_COMPUTED_SIZE);
				hboxMensagem.setPrefWidth(HBox.USE_COMPUTED_SIZE);
				hboxMensagem.setMaxSize(HBox.USE_COMPUTED_SIZE, HBox.USE_COMPUTED_SIZE);
				hboxMensagem.setMinSize(HBox.USE_COMPUTED_SIZE, HBox.USE_COMPUTED_SIZE);
				HBox.setMargin(hboxMensagem, new Insets(0, 10, 0, 0));
				hboxMensagem.setAlignment(Pos.CENTER);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						hboxMensagem.getChildren().add(parentBalaoData);
					}
				});
				return hboxMensagem;
			} catch (IOException e) {
				e.printStackTrace();
			}

		return null;
	}
	
	private void requisitaConversas() {
		Vector<Object> requisicao = new Vector<>();
		requisicao.add("conversas");
		requisicao.add(user);
		
		try {
			ConnectionUtils.saida.writeObject(requisicao);
			ConnectionUtils.saida.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// #################Ações de Componentes################# //

	public void enviaMensagem() {
		LocalDateTime horario = LocalDateTime.now();
		String textoDaMensagem = taEscritura.getText();
		textoDaMensagem += new String(Character.toChars(0x1F349));
		taEscritura.clear();

		Vector<Object> requisicao = new Vector<>();
		
		Mensagem mensagemParaEnvio = new Mensagem(textoDaMensagem, user, ViewCentralController.getUserParaConversar(), horario);
		
		
		requisicao.add("mensagem");	
		requisicao.add(mensagemParaEnvio);
		requisicao.add(conversaAtual);
		
		colocaBalaoConversa(mensagemParaEnvio, 2);

		try {
			ConnectionUtils.saida.writeObject(requisicao);
			ConnectionUtils.saida.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(conversaAtual == null) {
			requisitaConversas();
		}
		else {
			atualizaVboxConversa(mensagemParaEnvio, mensagemParaEnvio.getDestinatario());
		}
	}

	public void recebeMensagem(Vector<?> requisicao) {
		Mensagem mensagemRecebida = (Mensagem) requisicao.get(1);
		if(mensagemRecebida.getRemetente().equals(userParaConversar)) {
			colocaBalaoConversa(mensagemRecebida, 1);
		}
		else {
			AlertUtils.showNotificacaoErroMensagem(mensagemRecebida);
		}	
		requisitaConversas();
	}
	
	@SuppressWarnings("unchecked")
	private void recebeHistoricoMensagem(Vector<?> requisicao) {
		limpaConversa();
		Vector<Mensagem>  historicoMensagens = (Vector<Mensagem>)requisicao.get(1);
		
		if(!historicoMensagens.isEmpty()) {
			LocalDateTime datamTeporaria = historicoMensagens.firstElement().getDateTime();
			colocaBalaoConversa(historicoMensagens.firstElement(), 3);
			
			for (Mensagem mensagemHistorico : historicoMensagens) {
				if(mensagemHistorico.getDateTime().toLocalDate().isAfter(datamTeporaria.toLocalDate())) {
					datamTeporaria = mensagemHistorico.getDateTime();
					colocaBalaoConversa(mensagemHistorico, 3);
				}
				if(mensagemHistorico.getRemetente().equals(user)) {
					colocaBalaoConversa(mensagemHistorico, 2);
				}
				else {
					colocaBalaoConversa(mensagemHistorico, 1);
				}	
			}	
		}
	}
	
	@SuppressWarnings("unchecked")
	private void recebeConversas(Vector<?> requisicao) throws IOException {
		Vector<Conversa> conversas = (Vector<Conversa>)requisicao.get(1);
		carregaVboxConversas(conversas);
	}

	@SuppressWarnings("unchecked")
	public void recebeBroadcast(Vector<?> requisicao) throws IOException {
		for (Usuario usuario : Colecao.usuariosAtivos) {
			if (!((Vector<Usuario>) requisicao.get(1)).contains(usuario)) {
				AlertUtils.showNotificacaoLogin(false, usuario.getUsuario());
			}
		}
		Colecao.usuariosAtivos = (Vector<Usuario>) requisicao.get(1);
		carregaVboxUsuariosLogados();
		if (!Colecao.usuariosAtivos.lastElement().equals(user)) {
			AlertUtils.showNotificacaoLogin(true, Colecao.usuariosAtivos.lastElement().getUsuario());
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		lbUser.setText(user.getNomeDeExibicao());
		
		associaComponentesStaticos();
		requisitaConversas();

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
						case "historico":
							recebeHistoricoMensagem(requisicao);
							break;
						case "conversas":
							recebeConversas(requisicao);
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
