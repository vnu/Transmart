package com.android.transmart.location;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.android.transmart.R;

public class LocationActivity extends Activity {

	protected static String TAG = "TRANSMART LocationActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locateme);
		Log.i(TAG, "LocateMe Activity Started");
	}

}
