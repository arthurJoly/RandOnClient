package com.insa.randon.controller;



import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.insa.randon.R;
import com.insa.randon.model.GoogleMap;
import com.insa.randon.model.Map;
import com.insa.randon.model.Hike;

public class MapActivity extends BaseActivity {	
	private Map map;
	private LocationManager locManager;
	private Hike newHike;
	TextView distanceTextView;
	TextView speedTextView;
	FollowHikeLocationListener locListener;
	Context context;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        
        map = new GoogleMap(); 
        setContentView(map.getLayoutId());
        map.setUpMap(this);
        
        distanceTextView = (TextView) findViewById(R.id.distance_textView);
        speedTextView = (TextView) findViewById(R.id.speed_textView);
        
        locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locListener = new FollowHikeLocationListener();                             
        locManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 500, 5, locListener);
        
        //Create a new hike
        //TODO : test if we are in creation mode or not
        newHike = new Hike();
        newHike.extendHike(new LatLng(45.781307, 4.873902));
        newHike.extendHike(new LatLng(45.783971, 4.880210));
        newHike.extendHike(new LatLng(45.785347, 4.872700));
        newHike.extendHike(new LatLng(45.783641, 4.864847));
        
        distanceTextView.setText(getString(R.string.distance_done)+String.valueOf(newHike.getDistance())+"m");
       
        map.initializeNewHike();//in creation mode
        map.showRoute(newHike.getCoordinates());//in following an already existing hike mode
    }
	
	public class FollowHikeLocationListener implements LocationListener{
	    @Override
	    public void onLocationChanged(Location location)
	    {    
	    	//TODO : test if we are in creation mode
	    	map.followingHike(new LatLng(location.getLatitude(),location.getLongitude()));
	    	newHike.extendHike(new LatLng(location.getLatitude(),location.getLongitude()));
	    	distanceTextView.setText(getString(R.string.distance_done)+String.valueOf(newHike.getDistance())+"m");
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
