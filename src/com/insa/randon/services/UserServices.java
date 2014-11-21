package com.insa.randon.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.insa.randon.utilities.RequestExecutor;
import com.insa.randon.utilities.ResultObject;
import com.insa.randon.utilities.TaskListener;

public class UserServices {
	private static final String URL_BASE = "";
	private static final String SERVICE_CREATE_ACCOUNT = "";
	
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
		String url = URL_BASE +SERVICE_CREATE_ACCOUNT;
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(PARAMETER_EMAIL, email));
		params.add(new BasicNameValuePair(PARAMETER_LOGIN, username));
		params.add(new BasicNameValuePair(PARAMETER_PASSWORD, password));
		//TODO: encode parameter values with URLEncoder
		
		new RequestExecutor(params, url, RequestExecutor.RequestType.POST, listener).execute();
		
	}

}
