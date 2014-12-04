package com.insa.randon.utilities;

public class ResultObject/* <T> */{

	// T data;
	private ErrorCode errCode;
	private String content = "";

	//TODO : we may need to change content type
	ResultObject(ErrorCode errCode, String content) {
		this.errCode = errCode;
		this.content = content;
	}

	public ErrorCode getErrCode() {
		return errCode;
	}

	public String getContent() {
		return content;
	}
	
}
