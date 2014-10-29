package com.insa.randon;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MapActivity extends FragmentActivity  {
	/**
	 * @param args
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        
        /*GoogleMap googleMap;
        LatLng myPosition;
        
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        googleMap = fm.getMap();
        googleMap.setMyLocationEnabled(true);
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

