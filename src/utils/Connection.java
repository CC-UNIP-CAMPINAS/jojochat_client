package utils;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection implements AutoCloseable{
	Socket conexao = null;
	String host;
	

	public Connection(String aHost, int aPort) throws UnknownHostException, IOException {
			this.conexao = new Socket(aHost, aPort);
			this.host = aHost;
	}
	
	public Socket getConnection() {
		return conexao;
	}
	
	@Override
	public void close() throws Exception {
		conexao.close();	
	}
}
