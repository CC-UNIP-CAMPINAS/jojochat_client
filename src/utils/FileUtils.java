package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

import gui.controllers.ViewCentralController;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.entities.Arquivo;

public class FileUtils {
	final static FileChooser fileChooser = new FileChooser();
	
	public static File mostraSeletorArquivos(Stage janela) {
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		File file = fileChooser.showOpenDialog(janela);
		return verificaTamanhoArquivo(file);
	}
	
	public static File verificaTamanhoArquivo(File arquivo) {
		if(arquivo != null) {
			if(arquivo.length() >= 8000000) {
				AlertUtils.showNotificacaoArquivoGrande(arquivo);
				return null;
			}
			else {
				System.out.println(conversorDeUnidade(arquivo));
				return arquivo;
			}
		}
		else {
			return null;
		}
	}
	
	public static File procuraArquivo(String caminhoBase, File arquivoBuscado) {
		File caminhoBuscado = new File(caminhoBase);
		File[] files = caminhoBuscado.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if(file.getName().equals(arquivoBuscado.getName())) {
            	return file;
            }
        }
        return arquivoBuscado;
	}
	
	public static String conversorDeUnidade(File arquivo) {
		DecimalFormat df = new DecimalFormat("#.00");
		if(arquivo.length() <= 1024) {
			String resultado = String.valueOf(arquivo.length());
			return resultado+"B";
		}
		if(arquivo.length() > 1024 && arquivo.length() <= 1048576) {
			double calculo = (arquivo.length()/1024.0);
			String resultado = String.valueOf(df.format(calculo));
			return resultado+"kB";
		}
		if(arquivo.length() > 1048576) {
			double calculo = (arquivo.length()/1048576.0);
			String resultado = String.valueOf(df.format(calculo));
			return resultado+"MB";
		}
		return null;
	}
	
	public static boolean isImg(File arquivo) {
		if(arquivo.getName().contains(".png") || arquivo.getName().contains(".jpg")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	@SuppressWarnings("resource")
	public static void copiaArquivo(File arquivo, String destino, LocalDateTime horario) throws IOException{
		criaDiretorio(destino);
		FileChannel sourceChannel = null;
	    FileChannel destinationChannel = null;
	    destino += File.separatorChar+"["+horario+"] - "+arquivo.getName();
	    try {
	        sourceChannel = new FileInputStream(arquivo).getChannel();
	        destinationChannel = new FileOutputStream(destino).getChannel();
	        sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
	        
	        ViewCentralController.setArquivoParaEnvio(new File(destino));
	    } catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
	        if (sourceChannel != null && sourceChannel.isOpen())
	            sourceChannel.close();
	        if (destinationChannel != null && destinationChannel.isOpen())
	            destinationChannel.close();
	   }
	}
	
	public static boolean criaDiretorio(String caminho) {
		File diretorio = new File(caminho);
		if(!diretorio.exists()) {
			diretorio.mkdirs();
			return false;
		}
		else {
			return true;
		}
	}
	
	public static boolean verificaArquivo(File arquivo) {
		if(arquivo.exists()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static byte[] fileToBytes(File arquivo) throws IOException {
		FileInputStream fis;
        try {
        	byte[] bFile = new byte[(int) arquivo.length()];
			fis = new FileInputStream(arquivo);
			fis.read(bFile);
	        fis.close();
	        return bFile;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String gravaArquivo(Arquivo arquivo, String destino) throws IOException{
		criaDiretorio(destino);
	    destino += File.separatorChar+arquivo.getLocalizacao().getName();
	    FileOutputStream fos = new FileOutputStream(destino);
        fos.write(arquivo.getConteudo());
        fos.close();
        return destino;
	}
}
