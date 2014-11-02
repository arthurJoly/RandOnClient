package com.insa.randon.controller;

import com.insa.randon.R;
import com.insa.randon.R.layout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HikeListFragment extends Fragment {
	View rootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		rootView = inflater.inflate(R.layout.fragment_hike_list, container, false);
		return rootView;
	}
}
