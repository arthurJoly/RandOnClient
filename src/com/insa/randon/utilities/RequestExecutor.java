package com.insa.randon.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;


public class RequestExecutor extends AsyncTask<Void, Void, ResultObject>{
	static final String COOKIES_HEADER = "Set-Cookie";
	
	RequestType requestType;
	String url;
	TaskListener listener;
	String paramsStringJson;

	static CookieManager cookieManager = new CookieManager();
	
	static {
		CookieHandler.setDefault(cookieManager);
	}
	
	public RequestExecutor(String params, String url, RequestType type, TaskListener listener){
		this.paramsStringJson = params;
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
		
		if (listener != null){
			if (result.getErrCode() == ErrorCode.OK){
				listener.onSuccess(result.getContent());
			} else {
				listener.onFailure(result.getErrCode());
			}
		}
	}
	
	/**
	 * Performs a Get request
	 * @return object containing request result
	 */
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
		
	/**
	 * Performs a POST request
	 * @return object containing request result
	 */
	private ResultObject executePOST(){
		
		HttpURLConnection urlConnection = null;
		ResultObject resultObject = null;
		
		try {
			//To test post method, you can use this link: "http://postcatcher.in/catchers/546f635e9ac9260200000109"
			//TODO : change with HttpsUrlConnection
			URL urlGet = new URL(url);
			urlConnection = (HttpURLConnection) urlGet.openConnection();
			urlConnection.setDoOutput(true);
		    urlConnection.setChunkedStreamingMode(0);
		    urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		    
		    if (paramsStringJson != null)
		    {
		    	OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
			    out.write(paramsStringJson.getBytes());
			    out.flush ();
			    out.close ();
		    }
		    
		    //read response
			String response = "";
			try {
				response = readInputStream(urlConnection.getInputStream());
			} catch (FileNotFoundException e){
				e.printStackTrace();
			}
					
			int code = urlConnection.getResponseCode();
			System.out.println("code : " + code);
			if (code == HttpURLConnection.HTTP_CREATED || code == HttpURLConnection.HTTP_OK){
				resultObject = new ResultObject(ErrorCode.OK, response);
			} else if (code == HttpURLConnection.HTTP_FORBIDDEN){
				resultObject = new ResultObject(ErrorCode.DENIED, "");				
			} else {
				resultObject = new ResultObject(ErrorCode.FAILED, "");
			}			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			resultObject = new ResultObject(ErrorCode.REQUEST_FAILED, "");
		} catch (IOException e) {
			e.printStackTrace();
			resultObject = new ResultObject(ErrorCode.REQUEST_FAILED, "");
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
	
	
	public enum RequestType{
		GET,
		POST	
	}
}
