package gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.input.MouseEvent;

public class ViewNodeEmojiController extends Node implements Initializable {

	@FXML
	private JFXButton emojiFeliz;

	@FXML
	private JFXButton emojiTriste;

	@FXML
	private JFXButton emojiApaixonado;

	@FXML
	private JFXButton emojiChorando;

	@FXML
	private JFXButton emojiTedio;

	@FXML
	private JFXButton emojiBeijo;

	@FXML
	private JFXButton emojiBravo;

	@FXML
	private JFXButton emojiUrso;

	@FXML
	private JFXButton emojiUrsoAmoroso;

	@FXML
	private JFXButton emojiMenina;

	@FXML
	private JFXButton emojiMeh;

	@FXML
	private JFXButton emojiUrsoBravo;

	public void getEmoji() {

	}

	private class MyEventHandler implements EventHandler<Event> {
		@Override
		public void handle(Event evento) {
			JFXButton botao = (JFXButton) evento.getSource();
			int posicaoCursor = ViewCentralController.taEscrituraStatic.getCaretPosition();
			String fraseAntes = ViewCentralController.taEscrituraStatic.getText(0, posicaoCursor);
			String fraseDepois = ViewCentralController.taEscrituraStatic.getText(posicaoCursor,
					ViewCentralController.taEscrituraStatic.getLength());
			if (posicaoCursor == 0) {
				fraseAntes = ViewCentralController.taEscrituraStatic.getText();
				fraseDepois = "";
			}
			ViewCentralController.taEscrituraStatic.setText(fraseAntes + botao.getText() + " " + fraseDepois);
			ViewCentralController.taEscrituraStatic.positionCaret(ViewCentralController.taEscrituraStatic.getLength());
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		emojiFeliz.addEventHandler(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
		emojiApaixonado.addEventHandler(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
		emojiBeijo.addEventHandler(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
		emojiBravo.addEventHandler(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
		emojiChorando.addEventHandler(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
		emojiMeh.addEventHandler(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
		emojiMenina.addEventHandler(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
		emojiTedio.addEventHandler(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
		emojiTriste.addEventHandler(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
		emojiUrso.addEventHandler(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
		emojiUrsoAmoroso.addEventHandler(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
		emojiUrsoBravo.addEventHandler(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
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