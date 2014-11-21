package com.insa.randon.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
		editTextUserName = (EditText) findViewById(R.id.editText_create_user_name);
		editTextPassword = (EditText) findViewById(R.id.editText_create_password);
		editTextConfirmPassword = (EditText) findViewById(R.id.editText_create_confirm_password);
		editTextEmail = (EditText) findViewById(R.id.editText_create_email);
	}

	public void onButtonClick(View view){
		//Check if all field are completed

		String email = editTextEmail.getText().toString();
		String username = editTextUserName.getText().toString();
		String password = editTextPassword.getText().toString();
		String confirmPassword = editTextConfirmPassword.getText().toString();

		boolean notEmpty = (email.equals("") && username.equals("") && password.equals("") && confirmPassword.equals(""));
		boolean passwordConfirmed = password.equals(confirmPassword);
		//TODO: check if email is valid that the field don't contain special characters
		
		if (notEmpty){
			Toast.makeText(context, R.string.empty_fields, Toast.LENGTH_SHORT).show();
		} else if (!passwordConfirmed){
			Toast.makeText(context, R.string.password_not_confirmed, Toast.LENGTH_SHORT).show();
		} else {
		
			//initialize task listener
			TaskListener accountCreationListener = new TaskListener() {

				@Override
				public void onSuccess(String content) {
					Toast.makeText(context, R.string.account_creation_success, Toast.LENGTH_SHORT).show();	
					Intent intent = new Intent(context, HomeActivity.class);
					startActivity(intent);
					finish();
				}

				@Override
				public void onFailure(ErrorCode errCode) {
					Toast.makeText(context, R.string.account_creation_failure, Toast.LENGTH_SHORT).show();					
				}
			};

			UserServices.createUser(username, password, email, accountCreationListener);
		}

	}

}
