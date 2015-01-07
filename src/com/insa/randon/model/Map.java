package com.insa.randon.model;

import java.util.List;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public abstract class Map {
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
	 * Draw a route on the map and center the map on it
	 * @param hike List of GPS coordinates which represents the hike
	 */
	public abstract void showRoute(List<LatLng> route);
	
	/**
	 * add a new empty polyline to the map
	 */
	public abstract void initializeNewHike();
	
	/**
	 * update current hike on map
	 * @param newPoint a GPS coordinate that is added to the current new hike
	 */
	public abstract void followingHike(LatLng newPoint);
	
	/**
	 * Center the map on location
	 * @param latLong Location to show up on map
	 */
	public abstract void centerOnLocation(LatLng latLong);
	
	/**
	 * Set the map to display a specific area 
	 * @param bounds bounds of the area to display
	 */
	public abstract void setBounds(LatLngBounds bounds);
	
}
