package com.insa.randon.utilities;

public class ResultObject/* <T> */{

	// T data;
	private ErrorCode errCode;
	private String content = "";

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
