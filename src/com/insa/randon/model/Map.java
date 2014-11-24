package com.insa.randon.model;

import java.util.List;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;

public abstract class Map {
	List<LatLng> currentNewRoute; //Hike that a user create
	
	/**
	 * Get layout id
	 * @return Layout resource id
	 */
	public abstract int	getLayoutId();
	
	/**
	 * Initialize the map
	 * @param actvity Activity which will show the map
	 */
	public abstract void setUpMap(Activity activity);
	
	/**
	 * Draw a route on the map
	 * @param hike List of GPS coordinates which represents the hike
	 */
	public abstract void showRoute(List<LatLng> route);
	
	/**
	 * update current hike on map
	 * @param newPoint a GPS coordinate that belongs to the currentNewRoute
	 */
	public abstract void followHike(LatLng newPoint);
	
}
