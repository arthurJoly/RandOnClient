package com.insa.randon.utilities;

public enum ErrorCode {
	OK("Operation succeded"),
	FAILED("Operation has failed"),
	CONNECTION_FAILED("Attempt to connect has failed");
	
	protected String message;
	
	ErrorCode(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return this.message;
	}
	
	
}

