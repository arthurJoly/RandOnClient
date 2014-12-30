package com.insa.randon.model;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.insa.randon.R;

public class GoogleMap extends Map {
	private static final int LINE_WIDTH = 5;
	private static final int ZOOM_LEVEL = 15;
	private com.google.android.gms.maps.GoogleMap googleMap = null;
	private Polyline line;
	private PolylineOptions lineOptions;
	private Context context;
	

	@Override
	public void setUpMap(Activity activity) {
		MapFragment fm = (MapFragment) activity.getFragmentManager().findFragmentById(R.id.map);
        googleMap = fm.getMap();
        googleMap.setMyLocationEnabled(true);
       
        context = activity;
   	}

	@Override
	public void showRoute(List<LatLng> route) {
		if (googleMap != null){

			int size = route.size();
			if(size>=2)
			{
				//route.add(route.get(0)); //Close the loop
				googleMap.addPolyline(new PolylineOptions()
			     .addAll(route)
			     .width(LINE_WIDTH)
			     .color(context.getResources().getColor(R.color.path)));		
			}	
		}
		
	}
	
	@Override
	public void initializeNewHike() {
	    lineOptions = new PolylineOptions()
	    .width(LINE_WIDTH)
	    .color(context.getResources().getColor(R.color.path));
	    line = googleMap.addPolyline(lineOptions);
	}
	
	@Override
	public void followingHike(LatLng newPoint){
		List<LatLng> points = line.getPoints();
		points.add(newPoint);
		line.setPoints(points);
	}

	@Override
	public int getLayoutId() {
		return R.layout.googlemap;
	}

	@Override
	public void centerOnMyLocation() {

        Location myLocation = googleMap.getMyLocation();
        LatLng myLatLong = new LatLng(myLocation.getLatitude(), myLocation.getLatitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLong));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL));
		
	}
	
}
