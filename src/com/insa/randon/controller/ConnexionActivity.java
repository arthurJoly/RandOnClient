package com.insa.randon.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.insa.randon.R;
import com.insa.randon.services.UserServices;
import com.insa.randon.utilities.ErrorCode;
import com.insa.randon.utilities.TaskListener;

public class ConnexionActivity extends Activity {
	private static final int NUMBER_OF_TRY = 3;
	private static final int SLEEPING_TIME = 5000;
	
	Button connexionButton;
	EditText editTextUserName;
	EditText editTextPassword;
	Context context;
	int tries; //count the number of attempt to connect
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connexion);
		context = this;
		
		tries=0;
		
		connexionButton = (Button) findViewById(R.id.button_connexion);
		editTextUserName = (EditText) findViewById(R.id.editText_user_name);
		editTextPassword = (EditText) findViewById(R.id.editText_password);
	}
	
	public void onButtonClick(View view){
		if(this.tries>=NUMBER_OF_TRY)
		{
			 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		     alertDialogBuilder.setMessage(R.string.too_many_attempt_to_connect);
		     alertDialogBuilder.setPositiveButton(R.string.positive_button, 
		     new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface arg0, int arg1) {
					SystemClock.sleep(SLEEPING_TIME);
		        }
		     });
		     AlertDialog alertDialog = alertDialogBuilder.create();
		     alertDialog.show();
		} else {
			//Check if all field are completed
			String username = editTextUserName.getText().toString();
			String password = editTextPassword.getText().toString();

			boolean empty = (username.equals("") || password.equals(""));
			
			if (empty){
				Toast.makeText(context, R.string.empty_fields, Toast.LENGTH_SHORT).show();
			} else{
			
				//initialize task listener
				TaskListener connectListener = new TaskListener() {

					@Override
					public void onSuccess(String content) {
						Toast.makeText(context, R.string.connexion_success, Toast.LENGTH_SHORT).show();					
						Intent intent = new Intent(context, HomeActivity.class);
						startActivity(intent);
						finish();
					}

					@Override
					public void onFailure(ErrorCode errCode) {
						Toast.makeText(context, R.string.connexion_fail, Toast.LENGTH_SHORT).show();					
					}
				};

				UserServices.connect(username, password, connectListener);
				this.tries++;
			}
		}
	}
	
	public void onClick(View view) {
		Intent intent = new Intent(context, CreateAccountActivity.class);
		startActivity(intent);
    } 
}
