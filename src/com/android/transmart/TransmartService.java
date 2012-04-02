package com.android.transmart;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class TransmartService extends Service {
	
	public void onCreate(){
		Log.i(Util.TAG,"Transmart Service : OnCreate()");
		AlarmManager am= (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		Intent TSIntent = new Intent(this,MyServiceReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this,0,TSIntent,PendingIntent.FLAG_CANCEL_CURRENT);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 30 * 1, pi);
        Log.i(Util.TAG,"Repeating Alarm set up with RTC_WAKEUP");
		
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
