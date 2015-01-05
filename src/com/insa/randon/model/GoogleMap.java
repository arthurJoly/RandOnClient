package com.insa.randon.model;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
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
	private boolean centerOnMylocation = true;
	private boolean ignoreNextChange = true; //we need to know when user detects a camera change, so we ignore the cahnges we make ourselves


	@Override
	public void setUpMap(Activity activity) {
		MapFragment fm = (MapFragment) activity.getFragmentManager().findFragmentById(R.id.map);
		googleMap = fm.getMap();
		googleMap.setMyLocationEnabled(true);
		googleMap.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {
			
			@Override
			public boolean onMyLocationButtonClick() {
				centerOnMylocation = true;
				ignoreNextChange = true;
				return false;
			}
		});
		
		googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {
			
			@Override
			public void onCameraChange(CameraPosition arg0) {
				if (ignoreNextChange){
					ignoreNextChange = false;
				} else {
					centerOnMylocation = false;
					ignoreNextChange = false;
				}				
			}
		});
		
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
		
		if (centerOnMylocation){
			centerOnLocation(newPoint);
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.googlemap;
	}

	@Override
	public void centerOnLocation(LatLng myLatLong) {
		ignoreNextChange = true;
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLong));
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL));

	}

}
