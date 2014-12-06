package com.insa.randon.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

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
		InputStream inputStream = null;
        ResultObject resultObject = null;
        try {
 
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
 
            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
 
            String json = "";
 
            // 3. build jsonObject
            JSONObject jsonObject = createJSONObjectToSend(params);
 
            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();
 
            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);
 
            // 6. set httpPost Entity
            httpPost.setEntity(se);
 
            // 7. Set some headers to inform server about the type of the content   
            httpPost.setHeader("Content-type", "application/json");
 
            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);
 
            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            int code = httpResponse.getStatusLine().getStatusCode();
            if (code == HttpURLConnection.HTTP_CREATED || code == HttpURLConnection.HTTP_OK){
				resultObject = new ResultObject(ErrorCode.OK, convertInputStreamToString(inputStream));
			} else if (code == HttpURLConnection.HTTP_FORBIDDEN){
				resultObject = new ResultObject(ErrorCode.ALREADY_EXISTS, "");				
			} else {
				resultObject = new ResultObject(ErrorCode.FAILED, "");
			}
            
        } catch (MalformedURLException e) {
	    	e.printStackTrace();
	    	resultObject = new ResultObject(ErrorCode.REQUEST_FAILED, "");
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	resultObject = new ResultObject(ErrorCode.REQUEST_FAILED, "");
	    }  finally {
	    	//if (urlConnection != null){
	    	//	urlConnection.disconnect();
	    	//}
	    }
 
        // 11. return result
        return resultObject;
        
		//To test post method, you can use this link: "http://postcatcher.in/catchers/546f635e9ac9260200000109"

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
	
	/*private static String writeParameters(List<NameValuePair> parameters){
		String lineToWrite = "";
		
		NameValuePair pair = parameters.get(0);
		lineToWrite += pair.getName() + "=" + pair.getValue();
		
		for (int i=1; i<parameters.size(); i++){
			pair = parameters.get(i);
			lineToWrite += "&" + pair.getName() + "=" + pair.getValue();
		}
		return lineToWrite;
	}*/
	
	private static JSONObject createJSONObjectToSend(List<NameValuePair> parameters)
	{
		JSONObject jsonObj = new JSONObject();
		
		NameValuePair pair = parameters.get(0);
		try {
			jsonObj.accumulate(pair.getName(), pair.getValue());
			
			for (int i=1; i<parameters.size(); i++){
				pair = parameters.get(i);
				jsonObj.accumulate(pair.getName(), pair.getValue());
			}
		} catch (Exception e) {
	    	e.printStackTrace();
        }
		
		return jsonObj;
	}
	
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
 
    } 
	
	public enum RequestType{
		GET,
		POST
	}
}
