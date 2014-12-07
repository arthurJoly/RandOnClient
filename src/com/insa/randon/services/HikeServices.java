package com.insa.randon.services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;
import com.insa.randon.utilities.ErrorCode;
import com.insa.randon.utilities.RequestExecutor;
import com.insa.randon.utilities.ResultObject;
import com.insa.randon.utilities.TaskListener;

public class HikeServices {
	private static final String URL_BASE = "https://randon.herokuapp.com" ;
	private static final String SERVICE_CREATE_HIKE = "/hike/create";
	private static final String SERVICE_OVERVIEW = "/hike/overview";
	
	private static final String PARAMETER_HIKE_NAME = "name";
	private static final String PARAMETER_COORDINATES = "coordinates";
	private static final String PARAMETER_PRIVATE = "isPrivate";
	private static final String PARAMETER_LATITUDE = "lat";
	private static final String PARAMETER_LONGITUDE = "long";
	
	/*
	 * Services
	 */

	public static void createHike(String hikeName, List<LatLng> coordinates, boolean isPrivate, TaskListener listener)
	{
		//build url
		String url = URL_BASE + SERVICE_CREATE_HIKE;
		
		try {		
			String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate(PARAMETER_HIKE_NAME, URLEncoder.encode(hikeName, "UTF-8"));
            
           /* LatLng latLng = coordinates.get(0);
            String coordinatesJSON ="[{\"" + PARAMETER_LATITUDE + "\":" + latLng.latitude + ", \"" + PARAMETER_LONGITUDE + "\":" + latLng.longitude + "}";
            			
			for (int i=1; i<coordinates.size(); i++){
				latLng = coordinates.get(i);
				coordinatesJSON +=",{\"" + PARAMETER_LATITUDE + "\":" + latLng.latitude + ", \"" + PARAMETER_LONGITUDE + "\":" + latLng.longitude + "}";
			}
			coordinatesJSON +="]";*/
			
            jsonObject.accumulate(PARAMETER_COORDINATES, coordinates); 
            jsonObject.accumulate(PARAMETER_PRIVATE, isPrivate); 
            json = jsonObject.toString();
			
			new RequestExecutor(json, url, RequestExecutor.RequestType.POST2, listener).execute();	
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			listener.onFailure(ErrorCode.FAILED);
		} catch (Exception e) {
	    	e.printStackTrace();
        }
	}
	
	public static ResultObject getHike(TaskListener listener)
	{
		//build url
		String url = URL_BASE + SERVICE_OVERVIEW;
		
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		RequestExecutor requestExecutor = new RequestExecutor(params, url, RequestExecutor.RequestType.POST, listener);
		requestExecutor.execute();
		
		ResultObject result=new ResultObject(ErrorCode.FAILED, "");
		try {
			result = requestExecutor.get(5000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
		
	}

}
