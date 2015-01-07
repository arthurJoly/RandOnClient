package com.insa.randon.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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
import com.insa.randon.services.Constants;

public class HikeSearchFragment extends Fragment {
	View rootView;
	ListView hikeSearchListView ;
	TaskListener getListHikeListener;
	TaskListener getSpecificHikeListener;
	
	Context context;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_hike_search, container, false);
		hikeSearchListView = (ListView) rootView.findViewById(R.id.hike_search_list);
		context=getActivity();
		
	    getListHikeListener = new TaskListener() {

			@Override
			public void onSuccess(String content) {
				//Set up the hikes list
				List<Hike> hikes = new ArrayList<Hike>();
				try {
					JSONObject hikesList = new JSONObject(content);
					JSONArray hikesArray = hikesList.getJSONArray(Constants.JSON_OBJECT);
					for(int i=0; i<hikesArray.length(); i++){
						JSONObject hike = hikesArray.getJSONObject(i);
						hikes.add(new Hike(hike.getString(Constants.JSON_HIKE_NAME),hike.getString(Constants.JSON_HIKE_ID))); 
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}		
				HikeListAdapter customAdapter = new HikeListAdapter(context, R.layout.search_list_item, hikes);
				hikeSearchListView.setAdapter(customAdapter);
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
					JSONObject specificHike = restultJSON.getJSONObject(Constants.JSON_OBJECT);
					
					//Parse JSON coordinates to create a list of LatLng
					JSONArray JSONCoordinates = specificHike.getJSONArray(Constants.PARAMETER_COORDINATES);
					List<LatLng> coordinates = Constants.parseCoordinates(JSONCoordinates);
										
					Hike hikeToConsult = new Hike(specificHike.getString(Constants.JSON_HIKE_NAME),coordinates, 0, 0, 0);
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
				

		//Get the hikes of the database
		//HikeServices.getClosestSharedHikes(new LatLng(45.785347, 4.872700), getListHikeListener);
		HikeServices.getHikesShared(getListHikeListener);
		
		hikeSearchListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Hike hike = (Hike) parent.getAdapter().getItem(position);
				HikeServices.getSpecificHike(hike.getId(),getSpecificHikeListener);
		    }
		});

		return rootView;
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
		        if (nameTextView != null) {
		        	nameTextView.setText(hike.getName());
		        }
		        if (distanceTextView != null) {
		        	distanceTextView.setText(String.valueOf(hike.getDistance()));
		        }
		    }
		    return view;
		}	
	}
}
