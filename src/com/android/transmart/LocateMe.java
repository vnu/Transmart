package com.android.transmart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class LocateMe extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locateme);
		Log.i(Util.TAG, "LocateMe Activity Started");
	}

	/**
	 * Method called by OnClick of HOME Button.
	 * Finishes the activity and takes it to Main Activity.
	 * @param view
	 */
	public void toTransmartHome(View view) {
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		Log.i(Util.TAG, "MyService Home Button finishing Activity");
		finish();
	}

}
