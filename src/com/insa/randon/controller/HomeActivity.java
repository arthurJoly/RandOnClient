package com.insa.randon.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.insa.randon.R;
import com.insa.randon.services.UserServices;

public class HomeActivity extends Activity {
	public static final String FRAGMENT_EXTRA = "fragmentToStart";
	Button newHike, hikeSearch, myHikes;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		getActionBar().setTitle(R.string.app_name);
		context = this;
		
		newHike = (Button) findViewById(R.id.button_open_newHike);
		hikeSearch = (Button) findViewById(R.id.button_open_hikeSearch);
		myHikes = (Button) findViewById(R.id.button_open_myHikes);		
	}
	
	
	public void onButtonClick(View view){
		Intent intent = new Intent(context, MainActivity.class);
		
		switch (view.getId()){
		case R.id.button_open_hikeSearch: 
			intent.putExtra(FRAGMENT_EXTRA, HikeSearchFragment.class.getName());
			break;
		case R.id.button_open_myHikes: 
			intent.putExtra(FRAGMENT_EXTRA, HikeListFragment.class.getName());
			break;
		case R.id.button_open_newHike: 
			intent.putExtra(FRAGMENT_EXTRA, NewHikeFragment.class.getName());
			break;
		}
		
		startActivity(intent);
		finish();
	}
	
    @Override
    public void onBackPressed() {
    	super.onBackPressed();
    	
    	//call logout service
    	//initialize task listener
    	
    	//TODO : what should we do when log out API call fails? We can't ask the user to try again...
    	
		UserServices.logout(null);
    }

}
