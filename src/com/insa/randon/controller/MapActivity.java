package com.insa.randon.controller;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.insa.randon.model.GoogleMap;
import com.insa.randon.model.Map;

public class MapActivity extends BaseActivity {	
	private Map map;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        map = new GoogleMap(); 
        setContentView(map.getLayoutId());
        map.setUpMap(this);
        
        //Create a new hike (a list of coordinate)
        List<LatLng> newHike = new ArrayList<LatLng>();
        newHike.add(new LatLng(45.781307, 4.873902));
        newHike.add(new LatLng(45.783971, 4.880210));
        newHike.add(new LatLng(45.785347, 4.872700));
        newHike.add(new LatLng(45.783641, 4.864847));
        
        map.showRoute(newHike);
        
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
