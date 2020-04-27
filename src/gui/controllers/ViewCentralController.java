package gui.controllers;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.Vector;

import com.jfoenix.controls.JFXButton;

import app.Main;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.entities.Arquivo;
import model.entities.Colecao;
import model.entities.Conversa;
import model.entities.Mensagem;
import model.entities.Usuario;
import model.entities.enumerados.Baloes;
import utils.AlertUtils;
import utils.ConnectionUtils;
import utils.ConversorDataUtils;
import utils.FileUtils;

public class ViewCentralController implements Initializable {
	Socket conexao;
	private static Usuario user;
	private static Usuario userParaConversar;
	private static Conversa conversaAtual;
	private static File arquivoParaEnvio;
	
	public static AnchorPane apCentralStatic;
	public static Pane paneOpacoStatic;

	
	@FXML
    private AnchorPane apCentral2;

    @FXML
    private BorderPane bpCentral;

    @FXML
    private AnchorPane apCentral;

    @FXML
    private AnchorPane apCentralInf;

    @FXML
    private TextArea taEscritura;

    @FXML
    private JFXButton btEnviar;

    @FXML
    private ImageView imgEnviar;

    @FXML
    private JFXButton btEmoji;

    @FXML
    private ImageView imgEmoji;

    @FXML
    private JFXButton btArquivo;

    @FXML
    private ImageView ImgArquivo;

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

    @FXML
    private TabPane tabPaneConversas;

    @FXML
    private Tab tabConversasSalvas;

    @FXML
    private ScrollPane scrollPaneUser;

    @FXML
    private VBox vbConversas;

    @FXML
    private Tab tabNovasConversas;

    @FXML
    private AnchorPane acTabNovasConversas;

    @FXML
    private VBox vbUsuariosLogadas;

    @FXML
    private Pane paneOpaco;

	// #################Gets/Sets################# //

	public static Usuario getUser() {
		return user;
	}

	public static void setUser(Usuario user) {
		ViewCentralController.user = user;
	}
	
	public static void setArquivoParaEnvio(File arquivoParaEnvio) {
		ViewCentralController.arquivoParaEnvio = arquivoParaEnvio;
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
		ViewCentralController.paneOpacoStatic = paneOpaco;
	}
	
	// #################Utilitarios do controlador################# //
	
	public void setAcaoComponentes() {
		taEscritura.setOnKeyPressed(event -> {
		    if (event.getCode() == KeyCode.ENTER) {
		        event.consume();//Evita que ele mande o enter no textArea
		        if (event.isShiftDown()) {
		        	taEscritura.appendText(System.getProperty("line.separator"));
		        } else {
		            if(!taEscritura.getText().isEmpty()){
		                enviaMensagem();
		                taEscritura.setText("");
		            }
		        }
		    }
		});
	}
	
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
	
	public void colocaBalaoConversa(Mensagem conversa, Baloes opcao) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if(opcao.equals(Baloes.BALAO_INFORMACAO)) {
					vbMensagem.getChildren().add(criaBalaoDeData(conversa));
				}
				if(opcao.equals(Baloes.BALAO_DESTINATARIO) || opcao.equals(Baloes.BALAO_REMETENTE)) {
					vbMensagem.getChildren().add(criaBalaoDeMensagem(conversa, opcao));
				}
				if(opcao.equals(Baloes.BALAO_ARQUIVO_DESTINATARIO) || opcao.equals(Baloes.BALAO_ARQUIVO_REMETENTE)) {
					vbMensagem.getChildren().add(criaBalaoDeArquivoMensagem(conversa, opcao));
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
	
	public HBox criaBalaoDeMensagem(Mensagem conversa, Baloes opcao) {
		String horarioFormatado = ConversorDataUtils.getTimeToString(conversa.getDateTime());
		
		if (opcao.equals(Baloes.BALAO_DESTINATARIO)) {
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
		} 
		
		if(opcao.equals(Baloes.BALAO_REMETENTE)) {
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
	
	public HBox criaBalaoDeArquivoMensagem(Mensagem conversa, Baloes opcao) {
		if (opcao.equals(Baloes.BALAO_ARQUIVO_DESTINATARIO)) {
			FXMLLoader balaoArquivoDestinatario;
			try {
				balaoArquivoDestinatario = new FXMLLoader(getClass().getResource("/gui/views/ViewBalaoArquivoDestinatario.fxml"));
				Parent parentBalaoArquivoDestinatario = (Parent) balaoArquivoDestinatario.load();
				ViewBalaoArquivoDestinatarioController controlador = balaoArquivoDestinatario.getController();
				controlador.setaInformacoesIniciais(conversa);

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
						hboxMensagem.getChildren().add(parentBalaoArquivoDestinatario);
					}
				});
				return hboxMensagem;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		
		if(opcao.equals(Baloes.BALAO_ARQUIVO_REMETENTE)) {
			FXMLLoader balaoArquivoRemetente;
			try {
				balaoArquivoRemetente = new FXMLLoader(getClass().getResource("/gui/views/ViewBalaoArquivoRemetente.fxml"));
				Parent parentBalaoArquivoRemetente = (Parent) balaoArquivoRemetente.load();
				ViewBalaoArquivoRemetenteController controlador = balaoArquivoRemetente.getController();
				controlador.setaInformacoes(conversa);

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
						hboxMensagem.getChildren().add(parentBalaoArquivoRemetente);
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

	public void selecionaArquivo(){
		arquivoParaEnvio = FileUtils.mostraSeletorArquivos(Main.primaryStage);
		String textoDaMensagem = "";
		LocalDateTime horario = LocalDateTime.now();
		if(arquivoParaEnvio != null) {
			AlertUtils chamador = new AlertUtils();
			textoDaMensagem = chamador.showJanelaConfirmacaoEnvio(arquivoParaEnvio, horario);	
		}
		
		if(arquivoParaEnvio != null) {
			try {
				Vector<Object> requisicao = new Vector<>();
				Mensagem mensagemParaEnvio = new Mensagem(textoDaMensagem, user, ViewCentralController.getUserParaConversar(), horario, new Arquivo(FileUtils.fileToBytes(arquivoParaEnvio), arquivoParaEnvio));
				
				requisicao.add("mensagemComArquivo");	
				requisicao.add(mensagemParaEnvio);
				requisicao.add(conversaAtual);
				
				ConnectionUtils.saida.writeObject(requisicao);
				ConnectionUtils.saida.reset();
				
				colocaBalaoConversa(mensagemParaEnvio, Baloes.BALAO_ARQUIVO_REMETENTE);
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
	
	public void enviaMensagem() {
		if(!taEscritura.getText().isEmpty()) {
			LocalDateTime horario = LocalDateTime.now();
			String textoDaMensagem = taEscritura.getText();
			Vector<Object> requisicao = new Vector<>();
			
			Mensagem mensagemParaEnvio = new Mensagem(textoDaMensagem, user, ViewCentralController.getUserParaConversar(), horario);	
			
			requisicao.add("mensagem");	
			requisicao.add(mensagemParaEnvio);
			requisicao.add(conversaAtual);
			
			colocaBalaoConversa(mensagemParaEnvio, Baloes.BALAO_REMETENTE);

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
	}

	public void recebeMensagem(Vector<?> requisicao, Baloes opcao) throws IOException {
		Mensagem mensagemRecebida = (Mensagem) requisicao.get(1);
		if(mensagemRecebida.getRemetente().equals(userParaConversar)) {
			if(opcao.equals(Baloes.BALAO_DESTINATARIO)) {
				colocaBalaoConversa(mensagemRecebida, Baloes.BALAO_DESTINATARIO);
			}
			if(opcao.equals(Baloes.BALAO_ARQUIVO_DESTINATARIO)) {
//				String caminho = FileUtils.getCaminhoArquivos()+File.separatorChar+String.valueOf(mensagemRecebida.getRemetente().getId());
//				caminho = FileUtils.gravaArquivo(mensagemRecebida.getArquivo(), caminho);
				
				colocaBalaoConversa(mensagemRecebida, Baloes.BALAO_ARQUIVO_DESTINATARIO);
			}	
		}
		else {
			AlertUtils.showNotificacaoNovaMensagem(mensagemRecebida);
		}	
		requisitaConversas();
	}
	
	@SuppressWarnings("unchecked")
	private void recebeHistoricoMensagem(Vector<?> requisicao) {
		limpaConversa();
		Vector<Mensagem>  historicoMensagens = (Vector<Mensagem>)requisicao.get(1);
		
		if(!historicoMensagens.isEmpty()) {
			LocalDateTime dataTeporaria = historicoMensagens.firstElement().getDateTime();
			colocaBalaoConversa(historicoMensagens.firstElement(), Baloes.BALAO_INFORMACAO);
			
			for (Mensagem mensagemHistorico : historicoMensagens) {
				if(mensagemHistorico.getDateTime().toLocalDate().isAfter(dataTeporaria.toLocalDate())) {
					dataTeporaria = mensagemHistorico.getDateTime();
					colocaBalaoConversa(mensagemHistorico, Baloes.BALAO_INFORMACAO);
				}
				if(mensagemHistorico.getArquivo() == null) {
					if(mensagemHistorico.getRemetente().equals(user)) {
						colocaBalaoConversa(mensagemHistorico, Baloes.BALAO_REMETENTE);
					}
					else {
						colocaBalaoConversa(mensagemHistorico, Baloes.BALAO_DESTINATARIO);
					}
				}
				else {
					if(mensagemHistorico.getRemetente().equals(user)) {
						colocaBalaoConversa(mensagemHistorico, Baloes.BALAO_ARQUIVO_REMETENTE);
					}
					else {
						colocaBalaoConversa(mensagemHistorico, Baloes.BALAO_ARQUIVO_DESTINATARIO);
					}
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
		setAcaoComponentes();
		
		scrollPaneMensagens.vvalueProperty().bind(vbMensagem.heightProperty());

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
							recebeMensagem(requisicao, Baloes.BALAO_DESTINATARIO);
							break;
						case "mensagemComArquivo":
							recebeMensagem(requisicao, Baloes.BALAO_ARQUIVO_DESTINATARIO);
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
