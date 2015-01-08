package com.insa.randon.controller;

import java.util.ArrayList;
import java.util.List;

import static com.insa.randon.services.Constants.JSON_HIKE_NAME;
import static com.insa.randon.services.Constants.JSON_OBJECT;
import static com.insa.randon.services.Constants.JSON_HIKE_ID;
import static com.insa.randon.services.Constants.JSON_HIKE_DURATION;
import static com.insa.randon.services.Constants.JSON_HIKE_LENGTH;
import static com.insa.randon.services.Constants.JSON_HIKE_DATE;
import static com.insa.randon.services.Constants.PARAMETER_COORDINATES;
import static com.insa.randon.services.Constants.JSON_HIKE_POSITIVE_HEIGHT_DIFF;
import static com.insa.randon.services.Constants.JSON_HIKE_NEGATIVE_HEIGHT_DIFF;
import static com.insa.randon.services.Constants.PARAMETER_AVERAGE_SPEED;
import static com.insa.randon.utilities.ParserTool.parseCoordinates;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.insa.randon.R;
import com.insa.randon.model.Hike;
import com.insa.randon.services.HikeServices;
import com.insa.randon.utilities.TaskListener;
import com.insa.randon.utilities.ErrorCode;

public class HikeSearchFragment extends Fragment {
	private static final int MIN_TIME_INTERVAL_MS = 3000;
	private static final int MIN_DISTANCE_INTERVAL_M = 3;
	
	private View rootView;
	private ListView hikeSearchListView ;
	private TextView noItemTextView;
	private TaskListener getListHikeListener;
	private TaskListener getSpecificHikeListener;
	private LocationManager locManager;
	private GetCurrentLocationListener locListener;
	private LatLng currentLocation;
	
	Context context;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_hike_search, container, false);
		hikeSearchListView = (ListView) rootView.findViewById(R.id.hike_search_list);
		noItemTextView = (TextView) rootView.findViewById(R.id.tv_no_item);
		context=getActivity();
		
	    getListHikeListener = new TaskListener() {

			@Override
			public void onSuccess(String content) {
				//Set up the hikes list
				System.out.println(content);
				List<Hike> hikes = new ArrayList<Hike>();
				try {
					JSONObject hikesList = new JSONObject(content);
					JSONArray hikesArray = hikesList.getJSONArray(JSON_OBJECT);
					for(int i=0; i<hikesArray.length(); i++){
						JSONObject hike = hikesArray.getJSONObject(i);
						hikes.add(new Hike(hike.getString(JSON_HIKE_NAME),hike.getString(JSON_HIKE_ID),hike.getString(JSON_HIKE_DURATION),(float)hike.getDouble(JSON_HIKE_LENGTH))); 
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}	
				
				if (hikes.size() > 0){
					hikeSearchListView.setVisibility(View.VISIBLE);
					noItemTextView.setVisibility(View.GONE);
					HikeListAdapter customAdapter = new HikeListAdapter(context, R.layout.search_list_item, hikes);
					hikeSearchListView.setAdapter(customAdapter);
				}

			}

			@Override
			public void onFailure(ErrorCode errCode) {
				if (errCode == ErrorCode.REQUEST_FAILED){
					Toast.makeText(context,R.string.request_failed, Toast.LENGTH_SHORT).show();
				} else if (errCode == ErrorCode.FAILED){
					Toast.makeText(context,R.string.fail_retrieving_hikes, Toast.LENGTH_SHORT).show();
				}
			}
		};
		
		getSpecificHikeListener = new TaskListener() {
			@Override
			public void onSuccess(String content) {
				try {
					System.out.println(content);
					JSONObject restultJSON = new JSONObject(content);
					JSONObject specificHike = restultJSON.getJSONObject(JSON_OBJECT);
					
					//Parse JSON coordinates to create a list of LatLng
					JSONArray JSONCoordinates = specificHike.getJSONArray(PARAMETER_COORDINATES);
					List<LatLng> coordinates = parseCoordinates(JSONCoordinates);
										
					Hike hikeToConsult = new Hike(specificHike.getString(JSON_HIKE_NAME),coordinates, (float)specificHike.getDouble(JSON_HIKE_LENGTH), specificHike.getString(JSON_HIKE_DURATION), (float)specificHike.getDouble(JSON_HIKE_POSITIVE_HEIGHT_DIFF), (float)specificHike.getDouble(JSON_HIKE_NEGATIVE_HEIGHT_DIFF), (float) specificHike.getDouble(PARAMETER_AVERAGE_SPEED), specificHike.getString(JSON_HIKE_DATE));
					Intent intent = new Intent(context, ConsultingHikeActivity.class);
	        		intent.putExtra(MapActivity.EXTRA_HIKE, hikeToConsult);
	        		startActivity(intent);
				} catch (JSONException e) {
					e.printStackTrace();
				}				
			}

			@Override
			public void onFailure(ErrorCode errCode) {
				if (errCode == ErrorCode.REQUEST_FAILED){
					Toast.makeText(context,R.string.request_failed, Toast.LENGTH_SHORT).show();
				} else if (errCode == ErrorCode.FAILED){
					Toast.makeText(context,R.string.request_failed, Toast.LENGTH_SHORT).show();
				}
			}
		};
		
		locManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
		locListener = new GetCurrentLocationListener(); 
		
		//Get the hikes of the database
		//check if GPS is enabled
		PackageManager pm = getActivity().getPackageManager();
		boolean hasGps = pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
		if(!hasGps){
			locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_INTERVAL_MS, MIN_DISTANCE_INTERVAL_M, locListener);
		} else if (hasGps && !locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			HikeServices.getHikesShared(getListHikeListener);
		} else {
			locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_INTERVAL_MS, MIN_DISTANCE_INTERVAL_M, locListener);
		}
		
		hikeSearchListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Hike hike = (Hike) parent.getAdapter().getItem(position);
				HikeServices.getSpecificHike(hike.getId(),getSpecificHikeListener);
		    }
		});

		return rootView;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (locManager != null){
			locManager.removeUpdates(locListener);
		}    
	}
	
	//--------------- LIST ADAPTER ----------------------------
	public class HikeListAdapter extends ArrayAdapter<Hike> {
	
		public HikeListAdapter(Context context, int textViewResourceId) {
		    super(context, textViewResourceId);
		}
	
		public HikeListAdapter(Context context, int resource, List<Hike> items) {
		    super(context, resource, items);
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
		    View view = convertView;
	
		    if (view == null) {
		        LayoutInflater vi;
		        vi = LayoutInflater.from(getContext());
		        view = vi.inflate(R.layout.search_list_item, null);
		    }
	
		    Hike hike = (Hike) getItem(position);
	
		    if (hike != null) {
		        TextView nameTextView = (TextView) view.findViewById(R.id.hike_name_item);
		        TextView distanceTextView = (TextView) view.findViewById(R.id.hike_distance_item);
		        TextView durationTextView = (TextView) view.findViewById(R.id.hike_duration_item);
		        if (nameTextView != null) {
		        	nameTextView.setText(hike.getName());
		        }
		        if (distanceTextView != null) {
		        	distanceTextView.setText(String.format("%.2f", hike.getDistance()));
		        }
		        if (durationTextView != null) {
		        	durationTextView.setText(hike.getDuration());
		        }
		    }
		    return view;
		}	
	}
	
	//------------------ LOCATION LISTENER ------------------------------------
		public class GetCurrentLocationListener implements LocationListener{
			@Override
			public void onLocationChanged(Location location)
			{    
				currentLocation=new LatLng(location.getLatitude(),location.getLongitude());
				HikeServices.getClosestSharedHikes(currentLocation, getListHikeListener);
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
