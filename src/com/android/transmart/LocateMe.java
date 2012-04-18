package com.android.transmart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class LocateMe extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locateme);
		Log.i(Util.TAG, "LocateMe Activity Started");
		if(TransmartService.latitude!=null){
			TextView latitude = (TextView)findViewById(R.id.latitude);
			latitude.setText(TransmartService.latitude);
			TextView longitude = (TextView)findViewById(R.id.longitude);
			longitude.setText(TransmartService.longitude);
			TextView accuracy = (TextView)findViewById(R.id.accuracy);
			accuracy.setText(TransmartService.accuracy);
			TextView altitude = (TextView)findViewById(R.id.altitude);
			altitude.setText(TransmartService.altitude);
			
		}
	}
	
	public void nearbyPlaces(View view) {
		Intent myIntent = new Intent(view.getContext(), PlaceActivity.class);
		startActivityForResult(myIntent, 0);
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
