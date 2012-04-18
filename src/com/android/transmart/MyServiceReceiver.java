package com.android.transmart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyServiceReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.i(Util.TAG,"MyServiceReceiver : on Receive()");
		AppService.acquireStaticLock(context);
		/*GBLastLocFinder lastloc = new GBLastLocFinder(context);
		Location location=lastloc.getLastBestLocation(1, 1000*15);
		double lat = location.getLatitude();
		double lng = location.getLongitude();
		String latitude = String.valueOf(lat);
		String longitude = String.valueOf(lng);
		String locDetails = "Lat:" + latitude + "Long:" + longitude;
		Log.i(Util.TAG, "on receiver: " + locDetails);*/
		
		context.startService(new Intent(context,AppService.class));		
	}

}
