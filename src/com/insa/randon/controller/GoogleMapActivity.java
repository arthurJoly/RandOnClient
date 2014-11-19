package com.insa.randon.controller;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.insa.randon.R;

import android.graphics.Color;
import android.os.Bundle;

public class GoogleMapActivity extends MapActivity {
	public static final int LINE_WIDTH = 3;
	
	private GoogleMap googleMap;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        
        MapFragment fm = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        googleMap = fm.getMap();
        googleMap.setMyLocationEnabled(true);
        
        //Create a new hike (a list of coordinate)
        List<LatLng> newHike = new ArrayList<LatLng>();
        newHike.add(new LatLng(45.781307, 4.873902));
        newHike.add(new LatLng(45.783971, 4.880210));
        newHike.add(new LatLng(45.785347, 4.872700));
        newHike.add(new LatLng(45.783641, 4.864847));
        this.showHike(newHike);
        
        /*
        LatLng myPosition;
        
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);

        if(location!=null){
        	double latitude = location.getLatitude();
        	double longitude = location.getLongitude();
        	//LatLng latLng = new LatLng(latitude, longitude);
        	myPosition = new LatLng(latitude, longitude);
        	CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(myPosition, 5);
        	googleMap.animateCamera(yourLocation);
        }*/
    }
	
	protected void showHike(List<LatLng> hike){
		int size = hike.size();
		if(size>=2)
		{
			hike.add(hike.get(0)); //Close the loop
			Polyline line = googleMap.addPolyline(new PolylineOptions()
		     .addAll(hike)
		     .width(LINE_WIDTH)
		     .color(Color.GREEN));		
		}	
	}
}
