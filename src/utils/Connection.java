package utils;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection implements AutoCloseable{
	private Socket conexaoServer = null;
	String host;
	

	public Connection(String aHost, int aPort) throws UnknownHostException, IOException {
			this.conexaoServer = new Socket(aHost, aPort);
			this.host = aHost;
	}
	
	public Socket getConnection() {
		return conexaoServer;
	}
	
	@Override
	public void close() throws Exception {
		conexaoServer.close();	
	}
}
