package com.insa.randon.controller;

import java.util.ArrayList;
import java.util.List;

import android.R.anim;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
	View rootView;
	ListView hikeSearchListView ;
	
	Context context;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_hike_search, container, false);
		hikeSearchListView = (ListView) rootView.findViewById(R.id.hike_search_list);
		context=getActivity();
			
		//TEST
	    TaskListener getListHikeListener = new TaskListener() {

			@Override
			public void onSuccess(String content) {
				Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();					
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

		ResultObject result = HikeServices.getHike(getListHikeListener);
		Toast.makeText(context, result.getContent(), Toast.LENGTH_SHORT).show();
		
		List<Hike> hikes = new ArrayList<Hike>();	
		hikes.add(new Hike("hike 1",12,5,5));
		hikes.add(new Hike("hike 2",24,5,5));
		
		ListAdapter customAdapter = new ListAdapter(context, R.layout.search_list_item, hikes);

		hikeSearchListView .setAdapter(customAdapter);
		
		return rootView;
	}
	
	public class ListAdapter extends ArrayAdapter<Hike> {

		public ListAdapter(Context context, int textViewResourceId) {
		    super(context, textViewResourceId);
		}

		public ListAdapter(Context context, int resource, List<Hike> items) {
		    super(context, resource, items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

		    View v = convertView;

		    if (v == null) {

		        LayoutInflater vi;
		        vi = LayoutInflater.from(getContext());
		        v = vi.inflate(R.layout.search_list_item, null);
		    }

		    Hike p = (Hike) getItem(position);

		    if (p != null) {

		        TextView tt = (TextView) v.findViewById(R.id.hike_name_item);
		        TextView tt1 = (TextView) v.findViewById(R.id.hike_distance_item);
		        if (tt != null) {
		            tt.setText(p.getName());
		        }
		        if (tt1 != null) {

		            tt1.setText(String.valueOf(p.getDistance()));
		        }
		    }

		    return v;

		}
		
	}

}
