package com.insa.randon.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.insa.randon.R;

public class CreateAccountActivity extends BaseActivity {
		Button createAccountButton;
		EditText editTextUserName;
		EditText editTextPassword;
		EditText editTextConfirmPassword;
		EditText editTextEmail;
		
		Context context;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_create_account);
			context=this;
			
			createAccountButton = (Button) findViewById(R.id.button_create_account);
			editTextUserName = (EditText) findViewById(R.id.editText_user_name);
			editTextPassword = (EditText) findViewById(R.id.editText_password);
			editTextConfirmPassword = (EditText) findViewById(R.id.editText_confirm_password);
			editTextEmail = (EditText) findViewById(R.id.editText_email);
		}
		
		public void onButtonClick(View view){
			//Check if all field are completed
			//Call webservices
			new AlertDialog.Builder(this)
		    .setTitle("Compte créé")
		    .setMessage("Votre compte a été créé avec succès!")
		    .setCancelable(false)
		    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		        	Intent intent = new Intent(context, HomeActivity.class);
					startActivity(intent);
					finish();
		        }
		     })
		    .setIcon(android.R.drawable.ic_dialog_alert)
		    .show();
			
		}

}
