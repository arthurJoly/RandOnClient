package com.insa.randon.services;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.insa.randon.utilities.ErrorCode;
import com.insa.randon.utilities.RequestExecutor;
import com.insa.randon.utilities.ResultObject;
import com.insa.randon.utilities.TaskListener;

public class HikeServices {
	private static final String URL_BASE = "https://randon.herokuapp.com" ;
	private static final String URL_HIKE = "/hike";
	private static final String SERVICE_CREATE_HIKE = "/create";
	private static final String SERVICE_OVERVIEW = "/overview";
	private static final String SERVICE_SPECIFIC_HIKE = "/specific";
	private static final String SERVICE_PROXIMITY = "/proximity";
	
	private static final String PARAMETER_HIKE_NAME = "name";
	private static final String PARAMETER_COORDINATES = "coordinates";
	private static final String PARAMETER_PRIVATE = "isPrivate";
	private static final String PARAMETER_LATITUDE = "lat";
	private static final String PARAMETER_LONGITUDE = "long";
	private static final String PARAMETER_HIKE_ID = "hikeId";
	
	static Gson gson = new Gson();
	/*
	 * Services
	 */

	/**
	 * Creates a hike in the database
	 * @param hikeName Name of the created hike
	 * @param coordinates Coordinates of the created hike
	 * @param isPrivate boolean that indicates if the hike is shared or not
	 * @param listener Listener to notify when the account is created
	 */
	public static void createHike(String hikeName, List<LatLng> coordinates, boolean isPrivate, TaskListener listener)
	{
		//build url
		String url = URL_BASE + URL_HIKE+ SERVICE_CREATE_HIKE;
		try {		
			String jsonParams = "";
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty(PARAMETER_HIKE_NAME, hikeName);
			JsonArray coordinatesJson = createJsonArrayCoordinates(coordinates);
			jsonObject.add(PARAMETER_COORDINATES, coordinatesJson);
			jsonObject.addProperty(PARAMETER_PRIVATE, isPrivate);
			
			jsonParams = gson.toJson(jsonObject);
			
			new RequestExecutor(jsonParams, url, RequestExecutor.RequestType.POST, listener).execute();	
		} catch (Exception e) {
	    	e.printStackTrace();
	    	listener.onFailure(ErrorCode.FAILED);
        }
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
	
	private static JsonArray createJsonArrayCoordinates(List<LatLng> coordinates)
	{
		JsonArray coordinatesJson = new JsonArray();
		LatLng point = coordinates.get(0);
		JsonObject jsonPoint = new JsonObject();
		jsonPoint.addProperty(PARAMETER_LATITUDE, point.latitude);
		jsonPoint.addProperty(PARAMETER_LONGITUDE, point.longitude);
		coordinatesJson.add(jsonPoint);
		for(int i=1 ; i<coordinates.size() ; i++)
		{
			point = coordinates.get(i);
			jsonPoint.remove(PARAMETER_LATITUDE);
			jsonPoint.remove(PARAMETER_LONGITUDE);
			jsonPoint.addProperty(PARAMETER_LATITUDE, point.latitude);
			jsonPoint.addProperty(PARAMETER_LONGITUDE, point.longitude);
			coordinatesJson.add(jsonPoint);
		}
		return coordinatesJson;
	}

}
