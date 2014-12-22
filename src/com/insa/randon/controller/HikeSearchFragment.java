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

import com.insa.randon.R;
import com.insa.randon.model.Hike;
import com.insa.randon.services.HikeServices;
import com.insa.randon.utilities.ResultObject;
import com.insa.randon.utilities.TaskListener;
import com.insa.randon.utilities.ErrorCode;

public class HikeSearchFragment extends Fragment {
	static final String JSON_OBJECT = "content";
	static final String JSON_HIKE_NAME = "name";
	static final String JSON_HIKE_ID = "_id";
	View rootView;
	ListView hikeSearchListView ;
	TaskListener getListHikeListener;
	
	Context context;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_hike_search, container, false);
		hikeSearchListView = (ListView) rootView.findViewById(R.id.hike_search_list);
		context=getActivity();
			
		//TEST
	    getListHikeListener = new TaskListener() {

			@Override
			public void onSuccess(String content) {
				//Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();					
			}

			@Override
			public void onFailure(ErrorCode errCode) {
				if (errCode == ErrorCode.REQUEST_FAILED){
					Toast.makeText(context,"request failed", Toast.LENGTH_SHORT).show();
				} else if (errCode == ErrorCode.FAILED){
					Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
				}
			}
		};

		//Get the hikes of the database
		ResultObject result = HikeServices.getHikesShared(getListHikeListener);
		List<Hike> hikes = new ArrayList<Hike>();
		try {
			JSONObject hikesList = new JSONObject(result.getContent());
			JSONArray hikesArray = hikesList.getJSONArray(JSON_OBJECT);
			for(int i=0; i<hikesArray.length(); i++){
				JSONObject hike = hikesArray.getJSONObject(i);
				hikes.add(new Hike(hike.getString(JSON_HIKE_NAME),hike.getString(JSON_HIKE_ID))); 
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	
		//Set up the hikes list
		HikeListAdapter customAdapter = new HikeListAdapter(context, R.layout.search_list_item, hikes);
		hikeSearchListView.setAdapter(customAdapter);
		hikeSearchListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Hike hike = (Hike) parent.getAdapter().getItem(position);
				ResultObject resultSpecificHike = HikeServices.getSpecificHike(hike.getId(),getListHikeListener);
				try {
					JSONObject restultJSON = new JSONObject(resultSpecificHike.getContent());
					JSONObject specificHike = restultJSON.getJSONObject(JSON_OBJECT);
					Hike hikeToConsult = new Hike(specificHike.getString(JSON_HIKE_NAME), 0, 0, 0);
					Intent intent = new Intent(context, ConsultingHikeActivity.class);
	        		intent.putExtra(MapActivity.EXTRA_HIKE, hikeToConsult);
	        		startActivity(intent);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
