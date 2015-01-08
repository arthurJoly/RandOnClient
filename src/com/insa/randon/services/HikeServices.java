package com.insa.randon.services;


import static com.insa.randon.services.Constants.PARAMETER_COORDINATES;
import static com.insa.randon.services.Constants.PARAMETER_HIKE_ID;
import static com.insa.randon.services.Constants.PARAMETER_HIKE_NAME;
import static com.insa.randon.services.Constants.PARAMETER_DATE;
import static com.insa.randon.services.Constants.PARAMETER_DURATION;
import static com.insa.randon.services.Constants.PARAMETER_LENGTH;
import static com.insa.randon.services.Constants.PARAMETER_POS_DIFF_HEIGHT;
import static com.insa.randon.services.Constants.PARAMETER_NEG_DIFF_HEIGHT;
import static com.insa.randon.services.Constants.PARAMETER_AVERAGE_SPEED;
import static com.insa.randon.services.Constants.PARAMETER_LATITUDE;
import static com.insa.randon.services.Constants.PARAMETER_LONGITUDE;
import static com.insa.randon.services.Constants.PARAMETER_PRIVATE;
import static com.insa.randon.services.Constants.SERVICE_CREATE_HIKE;
import static com.insa.randon.services.Constants.SERVICE_OVERVIEW;
import static com.insa.randon.services.Constants.SERVICE_PROXIMITY;
import static com.insa.randon.services.Constants.SERVICE_SPECIFIC_HIKE;
import static com.insa.randon.services.Constants.SERVICE_HIKE_EXIST;
import static com.insa.randon.services.Constants.URL_BASE;
import static com.insa.randon.services.Constants.URL_HIKE;
import static com.insa.randon.services.Constants.URL_HISTORY;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.insa.randon.model.Hike;
import com.insa.randon.utilities.ErrorCode;
import com.insa.randon.utilities.RequestExecutor;
import com.insa.randon.utilities.TaskListener;

public class HikeServices {
	
	static Gson gson = new Gson();
	/*
	 * Services
	 */

	/**
	 * Creates a hike in the database
	 * @param hike Hike object to send
	 * @param isPrivate boolean that indicates if the hike is shared or not
	 * @param listener Listener to notify when the account is created
	 */
	public static void createHike(Hike hike, boolean isPrivate, TaskListener listener)
	{
		//build url
		String url = URL_BASE + URL_HIKE+ SERVICE_CREATE_HIKE;
		try {		
			String jsonParams = "";
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty(PARAMETER_HIKE_NAME, hike.getName());
			JsonArray coordinatesJson = createJsonArrayCoordinates(hike.getCoordinates());
			jsonObject.add(PARAMETER_COORDINATES, coordinatesJson);
			jsonObject.addProperty(PARAMETER_PRIVATE, String.valueOf(isPrivate));
			jsonObject.addProperty(PARAMETER_DATE, hike.getDate());
			jsonObject.addProperty(PARAMETER_DURATION, hike.getDuration());
			jsonObject.addProperty(PARAMETER_LENGTH, String.valueOf(hike.getDistance()));
			jsonObject.addProperty(PARAMETER_POS_DIFF_HEIGHT, String.valueOf(hike.getPositiveDiffHeight()));
			jsonObject.addProperty(PARAMETER_NEG_DIFF_HEIGHT, String.valueOf(hike.getNegativeDiffHeight()));
			jsonObject.addProperty(PARAMETER_AVERAGE_SPEED, String.valueOf(hike.getAverageSpeed()));
			
			jsonParams = gson.toJson(jsonObject);
			
			new RequestExecutor(jsonParams, url, RequestExecutor.RequestType.POST, listener).execute();	
		} catch (Exception e) {
	    	e.printStackTrace();
	    	listener.onFailure(ErrorCode.FAILED);
        }
	}
	
	/**
	 * Creates a hike in the database
	 * @param hikeName Name of the created hike
	 * @param coordinates Coordinates of the created hike
	 * @param isPrivate boolean that indicates if the hike is shared or not
	 * @param listener Listener to notify when the account is created
	 */
	public static void hikeNameExist(String hikeName, TaskListener listener)
	{
		//build url
		String url = URL_BASE + URL_HIKE + SERVICE_HIKE_EXIST;
		
		List<NameValuePair> listParams = new ArrayList<NameValuePair>();
		listParams.add(new BasicNameValuePair(PARAMETER_HIKE_NAME, hikeName));
			
		new RequestExecutor(listParams, url, RequestExecutor.RequestType.GET, listener).execute();	
	}
	
	/**
	 * Get the list of all the shared hikes
	 * @param listener Listener to notify when the account is created
	 * @return an object of type ResultObject that contains a String object that represent the list of hikes in JSON format
	 */
	public static void getHikesShared(TaskListener listener)
	{
		//build url
		String url = URL_BASE + URL_HIKE + SERVICE_OVERVIEW;

		RequestExecutor requestExecutor = new RequestExecutor("", url, RequestExecutor.RequestType.GET, listener);
		requestExecutor.execute();
	}
	
	/**
	 * Get the list of the shared hikes that are the closest to a given position
	 * @param coordinates Coordinates from which we would like to find the closest shared hikes
	 * @param listener Listener to notify when the account is created
	 * @return an object of type ResultObject that contains a String object that represent the list of hikes in JSON format
	 */
	public static void getClosestSharedHikes(LatLng coordinates, TaskListener listener)
	{
		//build url
		String url = URL_BASE + URL_HIKE + SERVICE_OVERVIEW + SERVICE_PROXIMITY;

		List<NameValuePair> listParams = new ArrayList<NameValuePair>();
		listParams.add(new BasicNameValuePair(PARAMETER_LATITUDE, String.valueOf(coordinates.latitude)));
		listParams.add(new BasicNameValuePair(PARAMETER_LONGITUDE, String.valueOf(coordinates.longitude)));
		
		RequestExecutor requestExecutor = new RequestExecutor(listParams, url, RequestExecutor.RequestType.GET, listener);
		requestExecutor.execute();
	}
	
	/**
	 * Get all the information of one specific hike
	 * @param id Identifier of the hike we would like to get
	 * @param listener Listener to notify when the account is created
	 * @return an object of type ResultObject that contains a String object that represent a hike in JSON format
	 */
	public static void getSpecificHike(String id, TaskListener listener)
	{
		//build url
		String url = URL_BASE + URL_HIKE + SERVICE_SPECIFIC_HIKE;

		List<NameValuePair> listParams = new ArrayList<NameValuePair>();
		listParams.add(new BasicNameValuePair(PARAMETER_HIKE_ID, id));

		RequestExecutor requestExecutor = new RequestExecutor(listParams, url, RequestExecutor.RequestType.GET, listener);
		requestExecutor.execute();
	}
	
	/**
	 * Get the list of all the user hikes
	 * @param listener Listener to notify when the account is created
	 * @return an object of type ResultObject that contains a String object that represent the list of hikes in JSON format
	 */
	public static void getMyHikes(TaskListener listener)
	{
		//build url
		String url = URL_BASE + URL_HISTORY;

		RequestExecutor requestExecutor = new RequestExecutor("", url, RequestExecutor.RequestType.GET, listener);
		requestExecutor.execute();
	}
	
	private static JsonArray createJsonArrayCoordinates(List<LatLng> coordinates)
	{
		JsonArray coordinatesJson = new JsonArray();
		for(int i=0 ; i<coordinates.size() ; i++)
		{
			LatLng point = coordinates.get(i);
			JsonObject jsonPoint = new JsonObject();
			jsonPoint.addProperty(PARAMETER_LATITUDE, String.valueOf(point.latitude));
			jsonPoint.addProperty(PARAMETER_LONGITUDE, String.valueOf(point.longitude));
			coordinatesJson.add(jsonPoint);
		}
		return coordinatesJson;
	}

}
