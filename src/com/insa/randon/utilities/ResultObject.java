package com.insa.randon.utilities;

import android.text.Html;

public class ResultObject/* <T> */{

	// T data;
	private ErrorCode errCode;
	private String content = "";

	//TODO : we may need to change content type
	public ResultObject(ErrorCode errCode, String content) {
		this.errCode = errCode;
		this.content = Html.fromHtml(content).toString();;
	}

	public ErrorCode getErrCode() {
		return errCode;
	}

	public String getContent() {
		return content;
	}
	
}
