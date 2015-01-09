package com.insa.randon.controller;



import java.util.List;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.insa.randon.R;
import com.insa.randon.model.GoogleMap;
import com.insa.randon.model.Hike;
import com.insa.randon.model.Map;

public class ConsultingHikeActivity extends BaseActivity {
	Hike hike;
	Context context;
	List<LatLng> testCoordinates;
	
	private TextView nameTextView;
	private TextView distanceTextView;
	private TextView durationTextView;
	private TextView positiveDiffTextView;
	private TextView negativeDiffTextView;
	private TextView dateTextView;
	private TextView speedTextView;
	private ViewStub mapContainer;
	private Map map;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulting_hike);
        context=this;
        
        Intent intent = getIntent();
        hike = (Hike)intent.getParcelableExtra(MapActivity.EXTRA_HIKE);
        
        nameTextView = (TextView) findViewById(R.id.name_textView);
        distanceTextView = (TextView) findViewById(R.id.distance_textView);
        speedTextView = (TextView) findViewById(R.id.speed_textView);
        durationTextView = (TextView) findViewById(R.id.duration_textView);
        positiveDiffTextView = (TextView) findViewById(R.id.positive_diiference_textView);
        negativeDiffTextView = (TextView) findViewById(R.id.negative_difference_textView);
        dateTextView = (TextView) findViewById(R.id.date_textView);
        
        nameTextView.setText(hike.getName());
        distanceTextView.setText(String.format("%.2f",hike.getDistance()));
        speedTextView.setText(String.format("%.2f",hike.getAverageSpeed()));
        durationTextView.setText(hike.getDuration());
        positiveDiffTextView.setText(String.valueOf(hike.getPositiveDiffHeight()));
        negativeDiffTextView.setText(String.valueOf(hike.getNegativeDiffHeight()));
        dateTextView.setText(hike.getDate());
        
        map = new GoogleMap();      
		mapContainer = (ViewStub) findViewById(R.id.map_activity_container);
		mapContainer.setLayoutResource(map.getLayoutId());    
		mapContainer.inflate();
		map.setUpMap(this);

		map.showRoute(hike.getCoordinates());
		
		//Listen for the layout to be complete
		nameTextView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
            	List<LatLng> coordinates = hike.getCoordinates();
            	double minLatitude = coordinates.get(0).latitude;
            	double minLongitude = coordinates.get(0).longitude;
            	double maxLatitude =coordinates.get(0).latitude;
            	double maxLongitude = coordinates.get(0).longitude;
            	
            	for(int i=1 ; i<coordinates.size() ; i++){
            		if(minLatitude>coordinates.get(i).latitude){
            			minLatitude=coordinates.get(i).latitude;
            		}
            		if(minLongitude>coordinates.get(i).longitude){
            			minLongitude=coordinates.get(i).longitude;
            		}
            		if(maxLatitude<coordinates.get(i).latitude){
            			maxLatitude=coordinates.get(i).latitude;
            		}
            		if(maxLongitude<coordinates.get(i).longitude){
            			maxLongitude=coordinates.get(i).longitude;
            		}
            	}
            	
		    	LatLngBounds boundsToDisplay = new LatLngBounds(new LatLng(minLatitude, minLongitude), new LatLng(maxLatitude, maxLongitude));
				map.setBounds(boundsToDisplay);
             }
        });
	}	
	
	@Override
	public void onBackPressed(){
	    // TODO Auto-generated method stub
	    super.onBackPressed();
	    try {
	    	FragmentManager fragmentManager = getFragmentManager();
	    	fragmentManager.beginTransaction().remove(fragmentManager.findFragmentById(R.id.map)).commit();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}

