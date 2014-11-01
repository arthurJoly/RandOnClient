package com.insa.randon;


import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class MapActivity extends Activity  {
	private GoogleMap googleMap;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        
        MapFragment fm = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        googleMap = fm.getMap();
        googleMap.setMyLocationEnabled(true);

        
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
}

