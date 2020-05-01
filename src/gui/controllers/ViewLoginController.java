package gui.controllers;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;

import app.Main;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import model.entities.FormularioCadastro;
import model.entities.Usuario;
import utils.AlertUtils;
import utils.ConnectionUtils;
import utils.CryptUtils;
import utils.FileUtils;

public class ViewLoginController implements Initializable {

	private static Scene sceneCentral;
	private boolean parada = true;
	private File arquivoDeImagem = null;
	private final Image olhoFechado = new Image("/model/img/icons8-closed-eye-60.png");
	private final Image olhoAberto = new Image("/model/img/icons8-eye-60.png");

	@FXML
    private AnchorPane anchorCentral;

    @FXML
    private TextField tfLogin;

    @FXML
    private PasswordField pfSenha;

    @FXML
    private TextField tfSenhaVisivel;

    @FXML
    private JFXButton btLogin;

    @FXML
    private JFXButton btCadastro;

    @FXML
    private JFXButton btMostraSenha;

    @FXML
    private ImageView imgOlho;

    @FXML
    private AnchorPane apCentral;

    @FXML
    private TextField tfLoginCadastro;

    @FXML
    private TextField tfNomeUsuario;

    @FXML
    private PasswordField pfSenhaCadastro;

    @FXML
    private TextField tfSenhaVisivelCadastro;

    @FXML
    private TextField tfNomeArquivo;

    @FXML
    private JFXButton btSelecionaImagem;

    @FXML
    private JFXButton btCadastrar;

    @FXML
    private JFXButton btCancelar;

    @FXML
    private Circle circleProfile;

    @FXML
    private ImageView imgProfile;

    @FXML
    private JFXButton btMostraSenhaCadastro;

    @FXML
    private ImageView imgOlhoCadastro;

    @FXML
    private JFXSpinner spinCarregando;

    @FXML
    private Pane paneLateral;

    @FXML
    private JFXSpinner piCarregando;

	@SuppressWarnings("unchecked")
	public void fazLogin() {
		try {
			Vector<String> login = new Vector<>();
			login.add("login");
			login.add(tfLogin.getText());
			login.add(CryptUtils.sha256(pfSenha.getText()));
			ConnectionUtils.saida.writeObject(login);
			ConnectionUtils.saida.reset();

			piCarregando.setVisible(true);
			parada = true;

			Task<Void> tarefa = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					while (parada) {
						Thread.sleep(0);
					}
					piCarregando.setVisible(false);
					return null;
				}
			};

			Platform.runLater(() -> {
				Thread t = new Thread(tarefa);
				t.start();
			});

			Task<Void> acaoCarregarLogin = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					Vector<Object> resposta = (Vector<Object>) ConnectionUtils.entrada.readObject();
					Boolean permissaoDeLogin = (Boolean) resposta.get(0);
					if (permissaoDeLogin) {
						ViewCentralController.setUser((Usuario) resposta.get(1));
						Platform.runLater(() -> {
							carregaTelaPrincipal();
						});

					} else {
						AlertUtils.showNotificacaoErroLogin(resposta.size());
					}

					parada = false;

					return null;
				}
			};

			Platform.runLater(() -> {
				Thread t = new Thread(acaoCarregarLogin);
				t.start();
			});

		} catch (EOFException | SocketException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setaImagemProfile() {
    	arquivoDeImagem = FileUtils.mostraSeletorImagens(Main.primaryStage);
    	if(arquivoDeImagem != null) {
    		Image imagemPerfil = new Image(arquivoDeImagem.toURI().toString());
        	imgProfile.setVisible(false);
        	circleProfile.setVisible(true);
        	circleProfile.setFill(new ImagePattern(imagemPerfil));
        	tfNomeArquivo.setText(arquivoDeImagem.getName());
    	}
    }
	
	public void cadastroToLogin() {
		limpaCadastro();		
		TranslateTransition andador = new TranslateTransition(Duration.seconds(0.8), paneLateral);
		andador.setToX(anchorCentral.getLayoutX());
		andador.play();	
	}
	
	public void loginToCadastro() {
		TranslateTransition andador = new TranslateTransition(Duration.seconds(0.8), paneLateral);
		andador.setToX(paneLateral.getLayoutX() + (anchorCentral.getPrefWidth() - paneLateral.getPrefWidth()));
		andador.play();
	}
	
	public void requisitaCadastro() {
		if(tfSenhaVisivelCadastro.isVisible()) {
			mostraSenhaCadastro();
		}
		if(tfNomeUsuario.getText().isEmpty() || pfSenhaCadastro.getText().isEmpty() || tfLoginCadastro.getText().isEmpty()) {
			AlertUtils.showNotificacaoCadastro(1);
		}
		else {
			try {
				FormularioCadastro novoUsuario = null;
				if(arquivoDeImagem == null) {
					novoUsuario = new FormularioCadastro(tfNomeUsuario.getText(), tfLoginCadastro.getText(), CryptUtils.sha256(pfSenhaCadastro.getText()), null, arquivoDeImagem);
				}
				else {
					novoUsuario = new FormularioCadastro(tfNomeUsuario.getText(), tfLoginCadastro.getText(), CryptUtils.sha256(pfSenhaCadastro.getText()), FileUtils.fileToBytes(arquivoDeImagem), arquivoDeImagem);
				}
				Vector<Object> requisicao = new Vector<Object>();
				requisicao.add("cadastro");
				requisicao.add(novoUsuario);
				
				ConnectionUtils.saida.writeObject(requisicao);
				ConnectionUtils.saida.reset();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			parada = true;
			spinCarregando.setVisible(true);
			
			Task<Void> tarefa = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					while (parada) {
						Thread.sleep(0);
					}
					spinCarregando.setVisible(false);
					return null;
				}
			};

			Task<Void> acaoCarregaCadastro = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					Platform.runLater(() -> {
						Thread t = new Thread(tarefa);
						t.start();
					});
					
					@SuppressWarnings("unchecked")
					Vector<Object> resposta = (Vector<Object>) ConnectionUtils.entrada.readObject();
					Boolean respostaCadastro = (Boolean) resposta.get(0);
					
					Thread.sleep(3000);
					if (respostaCadastro) {
						Platform.runLater(() -> {
							AlertUtils.showNotificacaoCadastro(3);
							cadastroToLogin();
						});

					} else {
						AlertUtils.showNotificacaoCadastro(2);
					}

					parada = false;

					return null;
				}
			};

			Platform.runLater(() -> {
				Thread t = new Thread(acaoCarregaCadastro);
				t.start();
			});
		}	
	}
	
	public void carregaTelaPrincipal() {
		Parent fxmlCentral;
		try {
			fxmlCentral = FXMLLoader.load(getClass().getResource("/gui/views/ViewCentral.fxml"));
			sceneCentral = new Scene(fxmlCentral);
			Main.primaryStage.setResizable(true);
			Main.primaryStage.setMinWidth(872.0);
			Main.primaryStage.setMinHeight(570.0);
			Main.primaryStage.setMaximized(true);
			Main.primaryStage.setScene(sceneCentral);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void mostraSenhaCadastro() {
		if(tfSenhaVisivelCadastro.isVisible()) {
			pfSenhaCadastro.setText(tfSenhaVisivelCadastro.getText());
			tfSenhaVisivelCadastro.setVisible(false);
			imgOlhoCadastro.setImage(olhoFechado);
		}
		else {
			tfSenhaVisivelCadastro.setText(pfSenhaCadastro.getText());
			tfSenhaVisivelCadastro.setVisible(true);
			imgOlhoCadastro.setImage(olhoAberto);
		}
	}
	
	public void mostraSenha() {
		if(tfSenhaVisivel.isVisible()) {
			pfSenha.setText(tfSenhaVisivel.getText());
			tfSenhaVisivel.setVisible(false);
			imgOlho.setImage(olhoFechado);
		}
		else {
			tfSenhaVisivel.setText(pfSenha.getText());
			tfSenhaVisivel.setVisible(true);
			imgOlho.setImage(olhoAberto);
		}
	}
	
	public void limpaCadastro() {
		tfLoginCadastro.setText("");
		tfNomeUsuario.setText("");
		tfNomeArquivo.setText("");
		pfSenhaCadastro.setText("");
		tfSenhaVisivelCadastro.setText("");
		circleProfile.setVisible(false);
		imgProfile.setVisible(true);
		arquivoDeImagem = null;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
}