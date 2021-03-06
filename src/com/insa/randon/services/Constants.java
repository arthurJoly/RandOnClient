package com.insa.randon.services;


public final class Constants {

    private Constants() {
            // restrict instantiation
    }

	public static final String URL_BASE = "https://randon.herokuapp.com" ;
	
	public static final String URL_HIKE = "/hike";
	public static final String URL_HISTORY = "/history/overview";
	public static final String URL_USER = "/user";
	
	public static final String SERVICE_CREATE_ACCOUNT = "/register";
	public static final String SERVICE_CONNECT = "/login";
	public static final String SERVICE_LOGOUT = "/logout";
	public static final String SERVICE_CREATE_HIKE = "/create";
	public static final String SERVICE_OVERVIEW = "/overview";
	public static final String SERVICE_SPECIFIC_HIKE = "/specific";
	public static final String SERVICE_PROXIMITY = "/proximity";
	public static final String SERVICE_HIKE_EXIST = "/exists";
	
	public static final String PARAMETER_LOGIN = "username";
	public static final String PARAMETER_PASSWORD = "password";
	public static final String PARAMETER_EMAIL = "email";	
	public static final String PARAMETER_HIKE_NAME = "name";
	public static final String PARAMETER_COORDINATES = "coordinates";
	public static final String PARAMETER_PRIVATE = "isPrivate";
	public static final String PARAMETER_LATITUDE = "lat";
	public static final String PARAMETER_LONGITUDE = "long";
	public static final String PARAMETER_HIKE_ID = "hikeId";
	public static final String PARAMETER_DATE = "date";
	public static final String PARAMETER_DURATION = "duration";
	public static final String PARAMETER_LENGTH = "length";
	public static final String PARAMETER_POS_DIFF_HEIGHT = "positiveHeightDiff";
	public static final String PARAMETER_NEG_DIFF_HEIGHT = "negativeHeightDiff";
	public static final String PARAMETER_AVERAGE_SPEED = "averageSpeed";
	
	public static final String JSON_OBJECT = "content";
	public static final String JSON_HIKE_NAME = "name";
	public static final String JSON_HIKE_ID = "_id";
	public static final String JSON_HIKE_DURATION = "duration";
	public static final String JSON_HIKE_LENGTH = "length";
	public static final String JSON_HIKE_PROXIMITY = "proximity";
	public static final String JSON_HIKE_DATE = "date";
	public static final String JSON_HIKE_POSITIVE_HEIGHT_DIFF = "positiveHeightDiff";
	public static final String JSON_HIKE_NEGATIVE_HEIGHT_DIFF = "negativeHeightDiff";
}
