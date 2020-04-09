package utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptUtils {

	public static String sha256(String senha) {
		try {    
		       MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
		       byte messageDigest[] = algorithm.digest(senha.getBytes("UTF-8"));
		         
		       StringBuilder hexString = new StringBuilder();
		       for (byte b : messageDigest) {
		         hexString.append(String.format("%02X", 0xFF & b));
		       }
		       String senhahex = hexString.toString();
		        
		       return senhahex.toLowerCase();
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
