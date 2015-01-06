package com.insa.randon.services;

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
}
