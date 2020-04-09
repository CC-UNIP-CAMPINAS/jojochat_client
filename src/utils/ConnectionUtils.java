package utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ConnectionUtils implements AutoCloseable{
	private Socket conexaoServer = null;
	public static ObjectOutputStream saida;
    public static ObjectInputStream entrada;
	

	public ConnectionUtils(String aHost, int aPort) throws UnknownHostException, IOException {
			this.conexaoServer = new Socket(aHost, aPort);
	}
	
	public Socket getConnection() {
		return conexaoServer;
	}
	
	@Override
	public void close() throws Exception {
		conexaoServer.close();	
	}
}
