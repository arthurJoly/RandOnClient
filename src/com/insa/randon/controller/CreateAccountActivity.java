package com.insa.randon.controller;

import android.app.Activity;
import android.os.Bundle;

import com.insa.randon.R;

public class CreateAccountActivity extends Activity {
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_create_account);
			getActionBar().setTitle(R.string.app_name);
			
		}

}
