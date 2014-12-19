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
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.insa.randon.R;
import com.insa.randon.model.GoogleMap;
import com.insa.randon.model.Hike;
import com.insa.randon.model.Map;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

public class MapActivity extends BaseActivity {	
	private static final int MIN_TIME_INTERVAL_MS = 1000;
	private static final int MIN_DISTANCE_INTERVAL_M = 3;
	private static final int DELAY = 60000;
	private static final String DISTANCE_UNIT = " m";
	private static final String HOUR_UNIT = " h";
	private static final String MINUTE_UNIT = " min";
	private static final String SPEED_UNIT = " km/h";
	private static final float CONVERT_SPEED_UNIT_TO_KMH = (float) 0.06;
	
	private Map map;
	private LocationManager locManager;
	private Hike newHike;
	private TextView distanceTextView;
	private TextView speedTextView;
	private TextView durationTextView;
	private TextView positiveAltitudeTextView;
	private TextView negativeAltitudeTextView;
	private ImageView slidingIcon;
	private FollowHikeLocationListener locListener;
	private ViewStub mapContainer;
	private AlertDialog alert = null;
	
	Context context;
	long startTime = 0;
	
	//timer
	Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
    	float previousDistance=-1;
    	float speed=0;
    	
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;
            seconds = seconds % 60;
            minutes = minutes % 60;

            durationTextView.setText(String.format("%d"+HOUR_UNIT+" %d"+MINUTE_UNIT, hours, minutes));
            
            //COMPUTE SPEED
            if(previousDistance!=-1){
                speed = (newHike.getDistance()-previousDistance)/DELAY;
            }
            speedTextView.setText(String.format("%.2f", speed*CONVERT_SPEED_UNIT_TO_KMH) + SPEED_UNIT);
            previousDistance=newHike.getDistance();

            timerHandler.postDelayed(this, DELAY);
        }
    };
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        context=this;
         
        map = new GoogleMap();      
        mapContainer = (ViewStub) findViewById(R.id.map_activity_container);
        mapContainer.setLayoutResource(map.getLayoutId());    
        mapContainer.inflate();
        map.setUpMap(this);
        
        distanceTextView = (TextView) findViewById(R.id.distance_textView);
        speedTextView = (TextView) findViewById(R.id.speed_textView);
        durationTextView = (TextView) findViewById(R.id.duration_textView);
        positiveAltitudeTextView = (TextView) findViewById(R.id.positive_altitude_textView);
        negativeAltitudeTextView = (TextView) findViewById(R.id.negative_altitude_textView);
        positiveAltitudeTextView.setText("0"+DISTANCE_UNIT);
        negativeAltitudeTextView.setText("0"+DISTANCE_UNIT);
        distanceTextView.setText("0"+DISTANCE_UNIT);
        
        //setting PanelSlideListener listener
        SlidingUpPanelLayout sPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_panel_map);
        slidingIcon = (ImageView) findViewById(R.id.sliding_icon);
        sPanelLayout.setPanelSlideListener(new SlidingUpPanelListener(slidingIcon));
        
       
        locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locListener = new FollowHikeLocationListener(); 
        
        //Start timer
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
        
        
         //Create a new hike
        //TODO : test if we are in creation mode or not
        newHike = new Hike();
        
//        //HIKE TEST
//        newHike.extendHike(new LatLng(45.781307, 4.873902));
//        newHike.extendHike(new LatLng(45.783971, 4.880210));
//        newHike.extendHike(new LatLng(45.785347, 4.872700));
//        newHike.extendHike(new LatLng(45.783641, 4.864847));
//        
//        distanceTextView.setText(newHike.getDistance() + DISTANCE_UNIT);
       
        map.initializeNewHike();//in creation mode
        
        //map.showRoute(newHike.getCoordinates());//in following an already existing hike mode   
        
        
        //check if GPS is enabled
        PackageManager pm = getPackageManager();
        boolean hasGps = pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
        if(!hasGps){
        	locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_INTERVAL_MS, MIN_DISTANCE_INTERVAL_M, locListener);
        } else if (hasGps && !locManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            showAlertMessageNoGps();
        } else {
        	 locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_INTERVAL_MS, MIN_DISTANCE_INTERVAL_M, locListener);
        }

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
    
    @Override
    protected void onPause(){
    	super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    	
    }
    
    @Override
    protected void onResume(){
    	super.onResume();
        timerHandler.postDelayed(timerRunnable, 0);
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
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.map_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_finnish_hike:
            	timerHandler.removeCallbacks(timerRunnable); //Stop timer
        		Intent intent = new Intent(context, FinishHikeActivity.class);
        		intent.putExtra("hike", newHike);
        		startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
	
    //------------------ LOCATION LISTENER ------------------------------------
	public class FollowHikeLocationListener implements LocationListener{
		double currentAltitude=0;
    	double previousAltitude=Double.NEGATIVE_INFINITY;
    	double positiveHeightDifference=0;
    	double negativeHeightDifference=0;
    	
	    @Override
	    public void onLocationChanged(Location location)
	    {    
	    	//TODO : test if we are in creation mode
	    	//if we follow a hike that we downloaded maybe we can create a new hike like but we don't call followingHike
	    	map.followingHike(new LatLng(location.getLatitude(),location.getLongitude()));
	    	
	    	//if the instantaneous speed is null, we do not change the distance done
	    	if (location.getSpeed() > 0){
	    		newHike.extendHike(new LatLng(location.getLatitude(),location.getLongitude()));
		    	distanceTextView.setText(String.format("%.2f", newHike.getDistance()) + DISTANCE_UNIT);
	    	}
	    	
	    	//Compute the positive difference of height
	    	currentAltitude = location.getAltitude();
	    	if(currentAltitude>previousAltitude && previousAltitude!=Double.NEGATIVE_INFINITY)
	    	{
	    		positiveHeightDifference+=currentAltitude-previousAltitude;
	    		positiveAltitudeTextView.setText(String.format("%.2f", positiveHeightDifference)+DISTANCE_UNIT);
	    	} else if(currentAltitude<previousAltitude && previousAltitude!=Double.NEGATIVE_INFINITY){
	    		negativeHeightDifference+=currentAltitude-previousAltitude;
	    		negativeAltitudeTextView.setText(String.format("%.2f", negativeHeightDifference)+DISTANCE_UNIT);
	    	}
	    	previousAltitude=currentAltitude;
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
	
	//--------------- SlidingUpPlanel Listener
	private class SlidingUpPanelListener implements PanelSlideListener{
		private ImageView imageView;
		
		public SlidingUpPanelListener(ImageView imageView){
			this.imageView = imageView;
		}

		@Override
		public void onPanelSlide(View panel, float slideOffset) {
		}

		@Override
		public void onPanelCollapsed(View panel) {
			imageView.setImageResource(R.drawable.ic_action_collapse);
		}

		@Override
		public void onPanelExpanded(View panel) {
			imageView.setImageResource(R.drawable.ic_action_expand);
		}

		@Override
		public void onPanelAnchored(View panel) {
			
		}

		@Override
		public void onPanelHidden(View panel) {
			
		}

	}
}
