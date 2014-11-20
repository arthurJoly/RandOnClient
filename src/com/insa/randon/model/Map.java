package com.insa.randon.model;

import java.util.List;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;

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
	 * Draw a route on the map
	 * @param hike List of GPS coordinates which represents the hike
	 */
	public abstract void showRoute(List<LatLng> route);
	
}
