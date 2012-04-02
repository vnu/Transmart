package com.android.transmart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MyService extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myservice);
		Log.i(Util.TAG,"MyService Activity Started");
	}
	public void startService(View view){
		Log.i(Util.TAG,"StartService clicked");
		EditText text = (EditText)findViewById(R.id.timer);
		int min = Integer.parseInt(text.getText().toString());
		Bundle bundle = new Bundle();
		bundle.putInt("timer", min);
		Intent sIntent = new Intent(this,TransmartService.class);
		sIntent.putExtras(bundle);
		startService(sIntent);
	}
	public void toTransmartHome(View view){
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		Log.i(Util.TAG,"MyService Home Button finishing Activity");
		finish();
	}

}
