package com.insa.randon.services;

import java.util.ArrayList;
import java.util.List;

import com.insa.randon.utilities.RequestExecutor;
import com.insa.randon.utilities.ResultObject;

public class UserServices {
	private RequestExecutor executor;
	
	/*
	 * Constructor
	 */
	UserServices(){
		executor = new RequestExecutor();
	}
	
	/*
	 * Services
	 */
	public String createUser(String username, String password, String email)
	{
		List<String >params = new ArrayList<String>();
		ResultObject data = executor.executePOST(params, "someURL");
		
		return data.errCode.getMessage();
	}

}
