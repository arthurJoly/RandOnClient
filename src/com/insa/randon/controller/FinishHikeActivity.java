package com.insa.randon.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.insa.randon.R;
import com.insa.randon.model.Hike;

public class FinishHikeActivity extends BaseActivity {
	private static final String DISTANCE_UNIT = " m";
	
	private Hike hike;
	
	private TextView distanceTextView;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_hike);
        getActionBar().setTitle(R.string.app_name);
        
        Intent intent = getIntent();
        this.hike = (Hike)intent.getParcelableExtra("hike");
        
        distanceTextView = (TextView) findViewById(R.id.distance_done_finish_textView);
        distanceTextView.setText(this.hike.getDistance() + DISTANCE_UNIT);
        
	}
}
