package com.insa.randon;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class NewHikeFragment extends Fragment {
	private View rootView;
	private Button buttonNewHike;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_new_hike, container, false);
		buttonNewHike = (Button) rootView.findViewById(R.id.button_newHike);

		buttonNewHike.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mapActivity = new Intent(getActivity(), MapActivity.class);  
				startActivity(mapActivity);
			}
		});
		return rootView;
	}
}