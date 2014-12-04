package com.insa.randon.services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.insa.randon.utilities.ErrorCode;
import com.insa.randon.utilities.RequestExecutor;
import com.insa.randon.utilities.ResultObject;
import com.insa.randon.utilities.TaskListener;

public class UserServices {
	private static final String URL_BASE = "https://randon.herokuapp.com" ;
	private static final String SERVICE_CREATE_ACCOUNT = "/register";
	private static final String SERVICE_CONNECT = "/login";
	
	private static final String PARAMETER_LOGIN = "username";
	private static final String PARAMETER_PASSWORD = "password";
	private static final String PARAMETER_EMAIL = "email";
	
	/*
	 * Services
	 */
	
	/**
	 * Creates a user account
	 * @param username Username of the account to create
	 * @param password Password of the account
	 * @param email Email of the account owner
	 * @param listener Listener to notify when the account is created
	 */
	public static void createUser(String username, String password, String email, TaskListener listener)
	{
		//build url
		String url = URL_BASE + SERVICE_CREATE_ACCOUNT;
		
		try {
			String hashPassword = hashPassword(password);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(PARAMETER_EMAIL, URLEncoder.encode(email, "UTF-8")));
			params.add(new BasicNameValuePair(PARAMETER_LOGIN, URLEncoder.encode(username, "UTF-8")));
			params.add(new BasicNameValuePair(PARAMETER_PASSWORD, hashPassword));
			//TODO: encode parameter values with URLEncoder
			
			new RequestExecutor(params, url, RequestExecutor.RequestType.POST, listener).execute();	
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			listener.onFailure(ErrorCode.FAILED);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			listener.onFailure(ErrorCode.FAILED);
		}		
	}
	
	/**
	 * Connect user
	 * @param username Username of the user
	 * @param password Password of the user
	 * @param listener Listener to notify when the account is created
	 */
	public static void connect(String username, String password, TaskListener listener)
	{
		//build url
		String url = URL_BASE + SERVICE_CONNECT;
		
		try {
			String hashPassword = hashPassword(password);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(PARAMETER_LOGIN, URLEncoder.encode(username, "UTF-8")));
			params.add(new BasicNameValuePair(PARAMETER_PASSWORD, hashPassword));
	
			new RequestExecutor(params, url, RequestExecutor.RequestType.POST, listener).execute();	
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			listener.onFailure(ErrorCode.FAILED);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			listener.onFailure(ErrorCode.FAILED);
		}	
	}
	
	private static final String hashPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(password.getBytes("UTF-8"));
		
		return bytesToHex(hash);
	}
	
	final private static char[] hexArray = "0123456789ABCDEF".toCharArray();
	private static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}

}
