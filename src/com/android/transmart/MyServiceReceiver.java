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
		context.startService(new Intent(context,AppService.class));		
	}

}
