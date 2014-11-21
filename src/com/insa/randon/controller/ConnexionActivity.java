package com.insa.randon.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.insa.randon.R;
import com.insa.randon.services.UserServices;
import com.insa.randon.utilities.ErrorCode;
import com.insa.randon.utilities.TaskListener;

public class ConnexionActivity extends Activity {
	Button connexionButton;
	EditText editTextUserName;
	EditText editTextPassword;
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connexion);
		context = this;
		
		connexionButton = (Button) findViewById(R.id.button_connexion);
		editTextUserName = (EditText) findViewById(R.id.editText_user_name);
		editTextPassword = (EditText) findViewById(R.id.editText_password);
	}
	
	public void onButtonClick(View view){
		Intent intent = new Intent(context, HomeActivity.class);
		startActivity(intent);
		finish();
	}
	
	public void onClick(View view) {
		//initialize task listener
		TaskListener accountCreationListener = new TaskListener() {
			
			@Override
			public void onSuccess(String content) {
				Toast.makeText(context, R.string.account_creation_success, Toast.LENGTH_SHORT).show();				
			}
			
			@Override
			public void onFailure(ErrorCode errCode) {
				Toast.makeText(context, R.string.account_creation_failure, Toast.LENGTH_SHORT).show();					
			}
		};
		
		//TODO : check login information with a webservice call
		UserServices.createUser("toto", "toto", "toto", accountCreationListener);
		
		Intent intent = new Intent(context, CreateAccountActivity.class);
		startActivity(intent);
    } 
}
