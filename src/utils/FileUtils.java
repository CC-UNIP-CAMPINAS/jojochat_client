package utils;

import java.io.File;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileUtils {
	final static FileChooser fileChooser = new FileChooser();
	
	public static File mostraSeletorArquivos(Stage janela) {
		File file = fileChooser.showOpenDialog(janela);
		return file; 
	}
}
