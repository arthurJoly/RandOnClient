package com.insa.randon.utilities;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

public class SpinnerDialog extends DialogFragment{
	private String message;
	  public SpinnerDialog(String message) {
		  this.message=message;
	    }

	    @Override
	    public Dialog onCreateDialog(final Bundle savedInstanceState) {

	    	ProgressDialog dialog = new ProgressDialog(getActivity());
	        this.setStyle(STYLE_NO_TITLE, getTheme()); // You can use styles or inflate a view
	        dialog.setMessage(this.message); // set your messages if not inflated from XML

	        dialog.setCancelable(false);  

	        return dialog;
	    }
}
