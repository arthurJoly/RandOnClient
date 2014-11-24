package com.insa.randon.controller;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.insa.randon.model.GoogleMap;
import com.insa.randon.model.Map;

public class MapActivity extends BaseActivity {	
	private Map map;
	private LocationManager locManager;
	FollowHikeLocationListener locListener;
	Context context;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;

        map = new GoogleMap(); 
        setContentView(map.getLayoutId());
        map.setUpMap(this);
        
        locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locListener = new FollowHikeLocationListener();                             
        locManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 500, 5, locListener);
        
        //Create a new hike (a list of coordinate)
        List<LatLng> newHike = new ArrayList<LatLng>();
        newHike.add(new LatLng(45.781307, 4.873902));
        newHike.add(new LatLng(45.783971, 4.880210));
        newHike.add(new LatLng(45.785347, 4.872700));
        newHike.add(new LatLng(45.783641, 4.864847));
        
        map.showRoute(newHike);
    }
	
	public class FollowHikeLocationListener implements LocationListener{
	    @Override
	    public void onLocationChanged(Location location)
	    {                         
	    	map.followHike(new LatLng(location.getLatitude(),location.getLongitude()));
	    }

	    @Override
	    public void onProviderDisabled(String provider)
	    {

	    }

	    @Override
	    public void onProviderEnabled(String provider)
	    {

	    }

	    @Override
	    public void onStatusChanged(String provider, int status, Bundle extras)
	    {

	    }                
	}
}
