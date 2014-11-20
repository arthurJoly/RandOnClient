package com.insa.randon.utilities;

import java.util.List;

public class RequestExecutor {
	
	public ResultObject executeGET(List<String> params, String url){
		return new ResultObject();
	}
	
	public ResultObject executePOST(List<String> params, String url){
		return new ResultObject(ErrorCode.OK);
	}
}
