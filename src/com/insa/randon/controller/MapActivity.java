package com.insa.randon.controller;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.insa.randon.R;
import com.insa.randon.model.GoogleMap;
import com.insa.randon.model.Map;
import com.insa.randon.model.Hike;

public class MapActivity extends BaseActivity {	
	private static final int MIN_TIME_INTERVAL_MS = 1000;
	private static final int MIN_DISTANCE_INTERVAL_M = 3;
	private static final String DISTANCE_UNIT = " m";
	
	private Map map;
	private LocationManager locManager;
	private Hike newHike;
	private TextView distanceTextView;
	private TextView speedTextView;
	private FollowHikeLocationListener locListener;
	private ViewStub mapContainer;
	private AlertDialog alert = null;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        
        map = new GoogleMap();        
        
        mapContainer = (ViewStub) findViewById(R.id.map_activity_container);
        mapContainer.setLayoutResource(map.getLayoutId());    
        mapContainer.inflate();
        
        map.setUpMap(this);
        
        distanceTextView = (TextView) findViewById(R.id.distance_textView);
        speedTextView = (TextView) findViewById(R.id.speed_textView);
        
        locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locListener = new FollowHikeLocationListener(); 
        
        //check if GPS is enabled
        PackageManager pm = getPackageManager();
        boolean hasGps = pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
        if(!hasGps){
        	locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_DISTANCE_INTERVAL_M, MIN_DISTANCE_INTERVAL_M, locListener);
        } else if (hasGps && !locManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            showAlertMessageNoGps();
        } else {
        	 locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_DISTANCE_INTERVAL_M, MIN_DISTANCE_INTERVAL_M, locListener);
        }
        
       
        //Create a new hike
        //TODO : test if we are in creation mode or not
        newHike = new Hike();
        newHike.extendHike(new LatLng(45.781307, 4.873902));
        newHike.extendHike(new LatLng(45.783971, 4.880210));
        newHike.extendHike(new LatLng(45.785347, 4.872700));
        newHike.extendHike(new LatLng(45.783641, 4.864847));
        
        distanceTextView.setText(newHike.getDistance() + DISTANCE_UNIT);
       
        map.initializeNewHike();//in creation mode
        map.showRoute(newHike.getCoordinates());//in following an already existing hike mode
    }
	
    private void showAlertMessageNoGps() {
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage(R.string.gps_disabled)
	           .setCancelable(false)
	           .setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	                   finish();
	               }
	           })
	           .setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                    dialog.cancel();
	                    locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_DISTANCE_INTERVAL_M, MIN_DISTANCE_INTERVAL_M, locListener);    
	               }
	           });
	    alert = builder.create();
	    alert.show();
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	if (alert != null){
    		alert.dismiss();
    	}
    	if (locManager != null){
    		locManager.removeUpdates(locListener);
    	}    	
    }
	
	public class FollowHikeLocationListener implements LocationListener{
	    @Override
	    public void onLocationChanged(Location location)
	    {    
	    	//TODO : test if we are in creation mode
	    	map.followingHike(new LatLng(location.getLatitude(),location.getLongitude()));
	    	newHike.extendHike(new LatLng(location.getLatitude(),location.getLongitude()));
	    	distanceTextView.setText(newHike.getDistance() + DISTANCE_UNIT);
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
