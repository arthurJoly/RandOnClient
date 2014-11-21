package com.insa.randon.utilities;

public interface TaskListener {
	public void onSuccess(String content);
	public void onFailure(ErrorCode errCode);
}
