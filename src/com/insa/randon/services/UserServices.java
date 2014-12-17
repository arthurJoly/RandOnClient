package com.insa.randon.services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.insa.randon.utilities.ErrorCode;
import com.insa.randon.utilities.RequestExecutor;
import com.insa.randon.utilities.TaskListener;

public class UserServices {
	private static final String URL_BASE = "https://randon.herokuapp.com" ;

	private static final String URL_USER = "/user";
	private static final String SERVICE_CREATE_ACCOUNT = "/register";
	private static final String SERVICE_CONNECT = "/login";
	private static final String SERVICE_LOGOUT = "/logout";

	
	private static final String PARAMETER_LOGIN = "username";
	private static final String PARAMETER_PASSWORD = "password";
	private static final String PARAMETER_EMAIL = "email";
	
	static Gson gson = new Gson();
	
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
		String url = URL_BASE + URL_USER + SERVICE_CREATE_ACCOUNT;
		
		try {
			String hashPassword = hashPassword(password);
			
			String jsonParams = "";
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty(PARAMETER_EMAIL, URLEncoder.encode(email, "UTF-8"));
			jsonObject.addProperty(PARAMETER_LOGIN, URLEncoder.encode(username, "UTF-8"));
			jsonObject.addProperty(PARAMETER_PASSWORD, hashPassword);
			
			jsonParams = gson.toJson(jsonObject);
			//TODO: encode parameter values with URLEncoder
			
			new RequestExecutor(jsonParams, url, RequestExecutor.RequestType.POST, listener).execute();	
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
		String url = URL_BASE + URL_USER + SERVICE_CONNECT;
		
		try {
			String hashPassword = hashPassword(password);
			
			String jsonParams = "";
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty(PARAMETER_LOGIN, URLEncoder.encode(username, "UTF-8"));
			jsonObject.addProperty(PARAMETER_PASSWORD, hashPassword);
			jsonParams = gson.toJson(jsonObject);
	
			new RequestExecutor(jsonParams, url, RequestExecutor.RequestType.POST, listener).execute();	
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			listener.onFailure(ErrorCode.FAILED);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			listener.onFailure(ErrorCode.FAILED);
		}	
	}
	
	/**
	 * Disconnect user connected
	 * @param listener Listener to notify when the user is disconnected
	 */
	public static void logout(TaskListener listener){
		String url = URL_BASE + URL_USER + SERVICE_LOGOUT;
		new RequestExecutor(null, url, RequestExecutor.RequestType.POST, listener).execute();
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
