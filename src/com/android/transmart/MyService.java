package com.android.transmart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MyService extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myservice);
		Log.i(Util.TAG,"MyService Activity Started");
	}
	public void startService(View view){
		Log.i(Util.TAG,"StartService clicked");
		Intent sIntent = new Intent(this,TransmartService.class);
		startService(sIntent);
	}
	public void toTransmartHome(View view){
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		Log.i(Util.TAG,"MyService Home Button finishing Activity");
		finish();
	}

}
