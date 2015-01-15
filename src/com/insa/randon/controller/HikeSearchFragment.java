package com.insa.randon.controller;

import static com.insa.randon.services.Constants.JSON_HIKE_DATE;
import static com.insa.randon.services.Constants.JSON_HIKE_DURATION;
import static com.insa.randon.services.Constants.JSON_HIKE_ID;
import static com.insa.randon.services.Constants.JSON_HIKE_LENGTH;
import static com.insa.randon.services.Constants.JSON_HIKE_NAME;
import static com.insa.randon.services.Constants.JSON_HIKE_PROXIMITY;
import static com.insa.randon.services.Constants.JSON_HIKE_NEGATIVE_HEIGHT_DIFF;
import static com.insa.randon.services.Constants.JSON_HIKE_POSITIVE_HEIGHT_DIFF;
import static com.insa.randon.services.Constants.JSON_OBJECT;
import static com.insa.randon.services.Constants.PARAMETER_AVERAGE_SPEED;
import static com.insa.randon.services.Constants.PARAMETER_COORDINATES;
import static com.insa.randon.utilities.ParserTool.parseCoordinates;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.insa.randon.R;
import com.insa.randon.model.Hike;
import com.insa.randon.services.HikeServices;
import com.insa.randon.utilities.ErrorCode;
import com.insa.randon.utilities.SpinnerDialog;
import com.insa.randon.utilities.TaskListener;

public class HikeSearchFragment extends Fragment {
	private static final int MIN_TIME_INTERVAL_MS = 3000;
	private static final int MIN_DISTANCE_INTERVAL_M = 3;
	private static final boolean ENABLED_PROVIDERS_ONLY = true;
	private static final long LOCATION_TIME_OUT = 7000;

	private Button closestHikesButton;
	private View rootView;
	private ListView hikeSearchListView ;
	private TextView noItemTextView;
	private TaskListener getListHikeListener;
	private TaskListener getSpecificHikeListener;
	private LocationManager locManager;
	private GetCurrentLocationListener locListener;
	private LatLng currentLocation = null;
	private SpinnerDialog waitingSpinnerDialog;
	private boolean hasProximity;
	
	Context context;
	
	private Handler timerHandler = new Handler();
	private Runnable timerRunnable = new Runnable() {
		
		@Override
		public void run() {
			if (currentLocation == null){
				if (waitingSpinnerDialog != null & waitingSpinnerDialog.isVisible()){
					waitingSpinnerDialog.dismiss();
				}
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
				alertDialogBuilder.setMessage(R.string.no_location_found);
				alertDialogBuilder.setPositiveButton(R.string.try_again, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						FragmentManager fm = getFragmentManager();
						waitingSpinnerDialog = new SpinnerDialog(context.getString(R.string.retrieving_hikes));
						waitingSpinnerDialog.show(fm, "");
						timerHandler.postDelayed(timerRunnable, LOCATION_TIME_OUT);
					}
				});
				alertDialogBuilder.setNegativeButton(R.string.quit, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (locManager != null){
							locManager.removeUpdates(locListener);
						}		
					}
				});
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}			
		}
			
	};


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_hike_search, container, false);
		closestHikesButton = (Button) rootView.findViewById(R.id.button_closest_hikes);
		hikeSearchListView = (ListView) rootView.findViewById(R.id.hike_search_list);
		noItemTextView = (TextView) rootView.findViewById(R.id.tv_no_item);
		context=getActivity();

		getListHikeListener = new TaskListener() {

			@Override
			public void onSuccess(String content) {
				//Stop waiting dialog
				waitingSpinnerDialog.dismiss();

				//Set up the hikes list
				System.out.println(content);
				List<Hike> hikes = new ArrayList<Hike>();
				try {
					JSONObject hikesList = new JSONObject(content);
					JSONArray hikesArray = hikesList.getJSONArray(JSON_OBJECT);
					for(int i=0; i<hikesArray.length(); i++){
						JSONObject hike = hikesArray.getJSONObject(i);
						if (hike.has(JSON_HIKE_PROXIMITY)){
							hikes.add(new Hike(hike.getString(JSON_HIKE_NAME),hike.getString(JSON_HIKE_ID),hike.getString(JSON_HIKE_DURATION),(float)hike.getDouble(JSON_HIKE_LENGTH), (float)hike.getDouble(JSON_HIKE_PROXIMITY)));
						} else {
							hikes.add(new Hike(hike.getString(JSON_HIKE_NAME),hike.getString(JSON_HIKE_ID),hike.getString(JSON_HIKE_DURATION),(float)hike.getDouble(JSON_HIKE_LENGTH)));
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}	

				if (hikes.size() > 0){
					hikeSearchListView.setVisibility(View.VISIBLE);
					noItemTextView.setVisibility(View.GONE);
					
					int idLayout;
					if (hasProximity){
						idLayout = R.layout.search_list_item_proximity;
					} else {
						idLayout = R.layout.search_list_item;
					}
					HikeListAdapter customAdapter = new HikeListAdapter(context, idLayout, hikes);
					hikeSearchListView.setAdapter(customAdapter);
				}

			}

			@Override
			public void onFailure(ErrorCode errCode) {
				//Stop waiting dialog
				waitingSpinnerDialog.dismiss();
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
				//Stop waiting dialog
				try {
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
				} finally {
					waitingSpinnerDialog.dismiss();
				}
			}

			@Override
			public void onFailure(ErrorCode errCode) {
				//Stop waiting dialog
				waitingSpinnerDialog.dismiss();
				if (errCode == ErrorCode.REQUEST_FAILED){
					Toast.makeText(context,R.string.request_failed, Toast.LENGTH_SHORT).show();
				} else if (errCode == ErrorCode.FAILED){
					Toast.makeText(context,R.string.request_failed, Toast.LENGTH_SHORT).show();
				}
			}
		};

		//Start waiting dialog
		FragmentManager fm = getFragmentManager();
		waitingSpinnerDialog = new SpinnerDialog(context.getString(R.string.retrieving_hikes));
		waitingSpinnerDialog.show(fm, "");

		//Get the hikes of the database
		hasProximity = false;
		HikeServices.getHikesShared(getListHikeListener);

		closestHikesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Start waiting dialog
				FragmentManager fm = getFragmentManager();
				waitingSpinnerDialog = new SpinnerDialog(context.getString(R.string.retrieving_hikes));
				waitingSpinnerDialog.show(fm, "");

				if (currentLocation == null){
					locManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

					Criteria criteria = new Criteria();
					criteria.setAccuracy(Criteria.ACCURACY_COARSE);
					criteria.setPowerRequirement(Criteria.POWER_LOW);
					String provider = locManager.getBestProvider(criteria, ENABLED_PROVIDERS_ONLY);
					locListener = new GetCurrentLocationListener(); 
					timerHandler.postDelayed(timerRunnable, LOCATION_TIME_OUT);

					//check if GPS is a provider is found
					if (provider == null){
						waitingSpinnerDialog.dismiss();

						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
						alertDialogBuilder.setMessage(R.string.no_provider_found);
						alertDialogBuilder.setNeutralButton(R.string.neutral_button, null);
						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
					} else {
						locManager.requestLocationUpdates(provider, MIN_TIME_INTERVAL_MS, MIN_DISTANCE_INTERVAL_M, locListener);
					}
				} else {
					hasProximity = true;
					HikeServices.getClosestSharedHikes(currentLocation, getListHikeListener);
				}
				
			}
		});

		hikeSearchListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//Start waiting dialog
				FragmentManager fm = getFragmentManager();
				waitingSpinnerDialog = new SpinnerDialog(context.getString(R.string.retrieving_specific_hike));
				waitingSpinnerDialog.show(fm, "");

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
		int layoutId;

		public HikeListAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		public HikeListAdapter(Context context, int resource, List<Hike> items) {
			super(context, resource, items);
			layoutId = resource;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;

			if (view == null) {
				LayoutInflater vi;
				vi = LayoutInflater.from(getContext());
				view = vi.inflate(layoutId, null);
			}

			Hike hike = (Hike) getItem(position);

			if (hike != null) {
				TextView nameTextView = (TextView) view.findViewById(R.id.hike_name_item);
				TextView distanceTextView = (TextView) view.findViewById(R.id.hike_distance_item);
				TextView durationTextView = (TextView) view.findViewById(R.id.hike_duration_item);
				TextView proximityTextView = (TextView) view.findViewById(R.id.hike_proximity);
				
				if (nameTextView != null) {
					nameTextView.setText(hike.getName());
				}
				if (distanceTextView != null) {
					distanceTextView.setText(String.format("%.2f", hike.getDistance()));
				}
				if (durationTextView != null) {
					durationTextView.setText(hike.getDuration());
				}
				if (proximityTextView != null && hike.getProximity()>0){
					proximityTextView.setText(String.format("%.1f", hike.getProximity()));
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
			currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
			hasProximity = true;
			HikeServices.getClosestSharedHikes(currentLocation, getListHikeListener);
			
			if (locManager != null){
				locManager.removeUpdates(locListener);
			}
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
