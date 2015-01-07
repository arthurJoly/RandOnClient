package com.insa.randon.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

public final class Constants {

    private Constants() {
            // restrict instantiation
    }

	public static final String URL_BASE = "https://randon.herokuapp.com" ;
	
	public static final String URL_HIKE = "/hike";
	public static final String URL_USER = "/user";
	
	public static final String SERVICE_CREATE_ACCOUNT = "/register";
	public static final String SERVICE_CONNECT = "/login";
	public static final String SERVICE_LOGOUT = "/logout";
	public static final String SERVICE_CREATE_HIKE = "/create";
	public static final String SERVICE_OVERVIEW = "/overview";
	public static final String SERVICE_SPECIFIC_HIKE = "/specific";
	public static final String SERVICE_PROXIMITY = "/proximity";
	
	public static final String PARAMETER_LOGIN = "username";
	public static final String PARAMETER_PASSWORD = "password";
	public static final String PARAMETER_EMAIL = "email";	
	public static final String PARAMETER_HIKE_NAME = "name";
	public static final String PARAMETER_COORDINATES = "coordinates";
	public static final String PARAMETER_PRIVATE = "isPrivate";
	public static final String PARAMETER_LATITUDE = "lat";
	public static final String PARAMETER_LONGITUDE = "long";
	public static final String PARAMETER_HIKE_ID = "hikeId";
	
	public static final String JSON_OBJECT = "content";
	public static final String JSON_HIKE_NAME = "name";
	public static final String JSON_HIKE_ID = "_id";
	
	public static List<LatLng> parseCoordinates(JSONArray JSONCoordinates) throws JSONException{
		List<LatLng> coordinates = new ArrayList<LatLng>();
		for(int i=0 ; i<JSONCoordinates.length() ; i++){
			JSONObject oneCoordinate = (JSONObject)JSONCoordinates.get(i);
			coordinates.add(new LatLng(oneCoordinate.getDouble(PARAMETER_LATITUDE),oneCoordinate.getDouble(PARAMETER_LONGITUDE)));
		}
		
		return coordinates;
	}
}
