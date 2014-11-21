package com.insa.randon.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import android.os.AsyncTask;

public class RequestExecutor extends AsyncTask<Void, Void, ResultObject>{
	RequestType requestType;
	String url;
	List<NameValuePair> params;
	TaskListener listener;
	
	public RequestExecutor(List<NameValuePair> params, String url, RequestType type, TaskListener listener){
		this.params = params;
		this.url = url;
		this.requestType = type;
		this.listener = listener;
	}
	

	@Override
	protected ResultObject doInBackground(Void... params) {
		ResultObject resultObject = null;
		
		if (requestType == RequestType.GET){
			resultObject = executeGET();
		} else if (requestType == RequestType.POST){
			resultObject = executePOST();
		}
		
		return resultObject;			
	}
	
	@Override
	protected void onPostExecute(ResultObject result) {
		super.onPostExecute(result);
		
		if (result.getErrCode() == ErrorCode.OK){
			listener.onSuccess(result.getContent());
		} else {
			listener.onFailure(result.getErrCode());
		}
		
	}
	
	private ResultObject executeGET(){
		HttpURLConnection urlConnection = null;
		ResultObject resultObject = null;
		
		try {
			//To test get method, you can use this link: "http://blogname.tumblr.com/api/read/json?num=5"
			URL urlGet = new URL(url);
			urlConnection = (HttpURLConnection) urlGet.openConnection();
			
			String response = readInputStream(urlConnection.getInputStream());
			int code = urlConnection.getResponseCode();
			if (code == HttpURLConnection.HTTP_OK){
				resultObject = new ResultObject(ErrorCode.OK, response);
			} else {
				resultObject = new ResultObject(ErrorCode.FAILED, "");
			}			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			resultObject = new ResultObject(ErrorCode.FAILED, "");
		} catch (IOException e) {
			e.printStackTrace();
			resultObject = new ResultObject(ErrorCode.FAILED, "");
		} finally {
			if (urlConnection != null){
				urlConnection.disconnect();
			}
		}

		return resultObject;
	}
		
	private ResultObject executePOST(){
		
		HttpURLConnection urlConnection = null;
		ResultObject resultObject = null;
		
		try {
			//To test post method, you can use this link: "http://postcatcher.in/catchers/546f635e9ac9260200000109"
			URL urlGet = new URL(url);
			urlConnection = (HttpURLConnection) urlGet.openConnection();
			urlConnection.setDoOutput(true);
		    urlConnection.setChunkedStreamingMode(0);
		    
		    //write parameters in outputstream
		    OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
		    out.write(writeParameters(params).getBytes());
		    out.flush ();
		    out.close ();

		    //read response
			String response = readInputStream(urlConnection.getInputStream());
			int code = urlConnection.getResponseCode();
			if (code == HttpURLConnection.HTTP_ACCEPTED || code == HttpURLConnection.HTTP_CREATED){
				resultObject = new ResultObject(ErrorCode.OK, response);
			} else {
				resultObject = new ResultObject(ErrorCode.FAILED, "");
			}			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			resultObject = new ResultObject(ErrorCode.FAILED, "");
		} catch (IOException e) {
			e.printStackTrace();
			resultObject = new ResultObject(ErrorCode.FAILED, "");
		} finally {
			if (urlConnection != null){
				urlConnection.disconnect();
			}
		}

		return resultObject;
	}
	
	private static String readInputStream(InputStream in) throws IOException{
		InputStream bis = new BufferedInputStream(in);
		byte[] contents = new byte[1024];
		int bytesRead=0;
		String strFileContents = ""; 
		
		while( (bytesRead = bis.read(contents)) != -1){ 
			strFileContents += new String(contents, 0, bytesRead);               
		}
		return strFileContents;
	}
	
	private static String writeParameters(List<NameValuePair> parameters){
		String lineToWrite = "";
		
		NameValuePair pair = parameters.get(0);
		lineToWrite += pair.getName() + "=" + pair.getValue();
		
		for (int i=1; i<parameters.size(); i++){
			pair = parameters.get(i);
			lineToWrite += "&" + pair.getName() + "=" + pair.getValue();
		}
		return lineToWrite;
	}
	
	public enum RequestType{
		GET,
		POST
	}
}
