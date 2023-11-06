package com.pa.security;

import java.security.MessageDigest;

import org.apache.tomcat.util.codec.binary.Base64;


public class PAEncoder {
	public static String toSHA1(String str) {
		 String salt = "qwertyuiopasfghjklzxcvbnm;@1";
		 String result = null;
		 str += salt;
		 try {
			byte[] dataBytes = str.getBytes();
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			result = Base64.encodeBase64String(md.digest(dataBytes));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
		 
		 
		 return result;
	 }
	 
	 private static final String SALT = "qwertyuiopasfghjklzxcvbnm;@1";

	    public static String decode(String encodedPassword) {
	        try {
	            byte[] decodedBytes = Base64.decodeBase64(encodedPassword);
	            MessageDigest md = MessageDigest.getInstance("SHA-1");
	            byte[] digestBytes = md.digest(decodedBytes);

	            // Remove the salt from the digest bytes.
	            byte[] passwordBytes = new byte[digestBytes.length - SALT.length()];
	            System.arraycopy(digestBytes, SALT.length(), passwordBytes, 0, passwordBytes.length);

	            return new String(passwordBytes);
	        } catch (Exception e) {
	            // TODO: handle exception
	            System.out.println(e);
	            return null;
	        }
	    }
  
}

