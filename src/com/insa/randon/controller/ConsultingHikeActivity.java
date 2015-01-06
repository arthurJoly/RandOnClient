package com.insa.randon.controller;



import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
        nameTextView.setText(hike.getName());
        distanceTextView.setText(String.valueOf(hike.getDistance()));
        
        map = new GoogleMap();      
		mapContainer = (ViewStub) findViewById(R.id.map_activity_container);
		mapContainer.setLayoutResource(map.getLayoutId());    
		mapContainer.inflate();
		map.setUpMap(this);
		

		//TODO : change with hike.getCoordinates() when there will be no longer the same coordinates in the lists
		testCoordinates = new ArrayList<LatLng>();
		testCoordinates.add(new LatLng(45.781307, 4.873902));
		testCoordinates.add(new LatLng(45.783971, 4.880210));
		testCoordinates.add(new LatLng(45.785347, 4.872700));
		testCoordinates.add(new LatLng(45.783641, 4.864847));

		map.showRoute(testCoordinates);
		
		//Listen for the layout to be complete
		nameTextView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
            	double minLatitude = testCoordinates.get(0).latitude;
            	double minLongitude = testCoordinates.get(0).longitude;
            	double maxLatitude =testCoordinates.get(0).latitude;
            	double maxLongitude = testCoordinates.get(0).longitude;
            	
            	for(int i=1 ; i<testCoordinates.size() ; i++){
            		if(minLatitude>testCoordinates.get(i).latitude){
            			minLatitude=testCoordinates.get(i).latitude;
            		}
            		if(minLongitude>testCoordinates.get(i).longitude){
            			minLongitude=testCoordinates.get(i).longitude;
            		}
            		if(maxLatitude<testCoordinates.get(i).latitude){
            			maxLatitude=testCoordinates.get(i).latitude;
            		}
            		if(maxLongitude<testCoordinates.get(i).longitude){
            			maxLongitude=testCoordinates.get(i).longitude;
            		}
            	}
            	
		    	LatLngBounds boundsToDisplay = new LatLngBounds(new LatLng(minLatitude, minLongitude), new LatLng(maxLatitude, maxLongitude));
				map.setBounds(boundsToDisplay);
             }
        });

		//map.showRoute(hike.getCoordinates());
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.consulting_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_download_hike:

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

