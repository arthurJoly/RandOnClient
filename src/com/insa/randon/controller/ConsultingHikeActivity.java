package com.insa.randon.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.insa.randon.R;
import com.insa.randon.model.Hike;

public class ConsultingHikeActivity extends BaseActivity {
	Hike hike;
	
	private TextView nameTextView;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulting_hike);
        
        Intent intent = getIntent();
        hike = (Hike)intent.getParcelableExtra(MapActivity.EXTRA_HIKE);
        
        nameTextView = (TextView) findViewById(R.id.name_textView);
        nameTextView.setText(hike.getName());
	}

}
