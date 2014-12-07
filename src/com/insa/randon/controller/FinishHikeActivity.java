package com.insa.randon.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.insa.randon.R;
import com.insa.randon.model.Hike;
import com.insa.randon.services.HikeServices;
import com.insa.randon.utilities.ErrorCode;
import com.insa.randon.utilities.ResultObject;
import com.insa.randon.utilities.TaskListener;

public class FinishHikeActivity extends BaseActivity {
	private static final String DISTANCE_UNIT = " m";
	
	Context context;
	private Hike hike;
	
	private TextView distanceTextView;
	private EditText nameEditText;
	private Button shareButton;
	private Button backHomeButton;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_hike);
        getActionBar().setTitle(R.string.app_name);
        
        context = this;
        
        Intent intent = getIntent();
        this.hike = (Hike)intent.getParcelableExtra("hike");
        
        distanceTextView = (TextView) findViewById(R.id.distance_done_finish_textView);
        nameEditText = (EditText) findViewById(R.id.editText_hike_name);
        shareButton = (Button) findViewById(R.id.button_share_hike);
        backHomeButton = (Button) findViewById(R.id.button_back_home);
        
        distanceTextView.setText(this.hike.getDistance() + DISTANCE_UNIT);        
	}
	
	public void onButtonClick(View view) {
		if(view == shareButton) //We save the hike and share it
		{
			TaskListener createHikeListener = new TaskListener() {

				@Override
				public void onSuccess(String content) {
					Toast.makeText(context, R.string.share_hike_succeded, Toast.LENGTH_SHORT).show();					
					
				}

				@Override
				public void onFailure(ErrorCode errCode) {
					if (errCode == ErrorCode.REQUEST_FAILED){
						Toast.makeText(context,R.string.request_failed, Toast.LENGTH_SHORT).show();
					} else if (errCode == ErrorCode.FAILED){
						Toast.makeText(context, R.string.request_failed, Toast.LENGTH_SHORT).show();
					}
				}
			};

			if(!nameEditText.getText().toString().isEmpty()){
				HikeServices.createHike(nameEditText.getText().toString(), hike.getCoordinates(),false, createHikeListener);
			} else {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			    alertDialogBuilder.setMessage(R.string.name_your_hike);
			    alertDialogBuilder.setNeutralButton(R.string.neutral_button, null);
			    AlertDialog alertDialog = alertDialogBuilder.create();
			    alertDialog.show();
			}
		}
		else if (view == backHomeButton) //We just save the hike in the history of the user
		{
			//TEST
			TaskListener getListHikeListener = new TaskListener() {

				@Override
				public void onSuccess(String content) {
					Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();					
				}

				@Override
				public void onFailure(ErrorCode errCode) {
					if (errCode == ErrorCode.REQUEST_FAILED){
						Toast.makeText(context,"request failed", Toast.LENGTH_SHORT).show();
					} else if (errCode == ErrorCode.FAILED){
						Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
					}
				}
			};

			ResultObject result = HikeServices.getHike(getListHikeListener);
			Toast.makeText(context, result.getContent(), Toast.LENGTH_SHORT).show();
		} 
		//then we go back to the home page

    }
}
