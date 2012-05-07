package com.android.transmart;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class TransmartActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Log.i(Util.TAG, "TransmartActivity Started");
		
		InputStream in;
		try {
			in = this.getAssets().open("iperf");
			FileOutputStream f = new FileOutputStream(new File("/data/data/com.android.transmart", "iperf"));
			byte[] buffer = new byte[1024];
			int len = 0;
			while ( (len = in.read(buffer)) > 0 ) {
				f.write(buffer,0, len);
			}
			f.close();
			Runtime.getRuntime().exec("/system/bin/chmod 744 /data/data/com.android.transmart/iperf");
			Log.i("BootReceiver", "onbootreceiver: iperf is set.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception exc){
			exc.printStackTrace();
		}
	}

	// Button onClick method for service button

	/**
	 * Method called by Service button OnClick. Opens the MyService Activity
	 * that starts the Service.
	 * 
	 * @param view
	 */
	public void myService(View view) {
		Intent myIntent = new Intent(view.getContext(), MyService.class);
		startActivityForResult(myIntent, 0);
	}
	
	public void addDownload(View view) {
		Intent myIntent = new Intent(view.getContext(), AddDownloads.class);
		startActivityForResult(myIntent, 0);

	}
	
	/**
	 * Method called by Locate Me on Click.
	 * Calls the LocationActivity class. This classes defines and performs the various operations
	 * associated with user location.
	 * @param view
	 * 
	 */
	public void locateMe(View view) {
		Intent myIntent = new Intent(view.getContext(), LocateMe.class);
		startActivityForResult(myIntent, 0);
	}
}