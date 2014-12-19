package com.insa.randon.controller;

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

		boolean empty = (email.equals("") || username.equals("") || password.equals("") || confirmPassword.equals(""));
		boolean passwordConfirmed = password.equals(confirmPassword);
		boolean correctEmail = (!email.contains("/") && !email.contains("\\"));
		//TODO: check if email is valid that the field doesn't contain special characters
		
		if (empty){
			Toast.makeText(context, R.string.empty_fields, Toast.LENGTH_SHORT).show();
		} else if (!passwordConfirmed){
			Toast.makeText(context, R.string.password_not_confirmed, Toast.LENGTH_SHORT).show();
		} else if(!correctEmail){
			Toast.makeText(context, R.string.uncorrect_email, Toast.LENGTH_SHORT).show();
		} else{
		
			//initialize task listener
			TaskListener accountCreationListener = new TaskListener() {

				@Override
				public void onSuccess(String content) {
					Toast.makeText(context, R.string.account_creation_success, Toast.LENGTH_SHORT).show();	
					Intent intent = new Intent(context, HomeActivity.class);
					startActivity(intent);
					setResult(RESULT_OK);
					finish();
				}

				@Override
				public void onFailure(ErrorCode errCode) {
					if (errCode == ErrorCode.DENIED){
						Toast.makeText(context, R.string.account_creation_already_exists, Toast.LENGTH_SHORT).show();
					} else if (errCode == ErrorCode.REQUEST_FAILED){
						Toast.makeText(context, R.string.request_failed, Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(context, R.string.account_creation_failure, Toast.LENGTH_SHORT).show();
					}
										
				}
			};

			UserServices.createUser(username, password, email, accountCreationListener);
		}

	}

}
