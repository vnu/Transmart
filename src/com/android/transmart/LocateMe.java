package com.android.transmart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.transmart.location.LocationActivity;

public class LocateMe extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locateme);
		Log.i(Util.TAG,"LocateMe Activity Started");
	}
	public void startLocationActivity(View view){
		Log.i(Util.TAG,"LocationActivity clicked");
		Intent myIntent = new Intent(view.getContext(), LocationActivity.class);
		startActivityForResult(myIntent, 0);
		
	}
	public void toTransmartHome(View view){
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		Log.i(Util.TAG,"MyService Home Button finishing Activity");
		finish();
	}

}
