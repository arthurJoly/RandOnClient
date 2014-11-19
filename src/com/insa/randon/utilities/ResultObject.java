package com.insa.randon.utilities;

public class ResultObject/*<T>*/ {

		//T data;
		public ErrorCode errCode;
		
		ResultObject()
		{
			
		}
		
		ResultObject(ErrorCode errCode)
		{
			this.errCode=errCode;
		}
}
