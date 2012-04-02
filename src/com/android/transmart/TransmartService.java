package com.android.transmart;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class TransmartService extends Service {
	private int minutes;
	
	
	public void onCreate(){
		Log.i(Util.TAG,"Transmart Service : OnCreate()");
		
		
	}
	
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(Util.TAG,"TransmartService : OnStartCOmmand()");
		Bundle bundle = intent.getExtras();
		minutes = bundle.getInt("timer");
		
		AlarmManager am= (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		Intent TSIntent = new Intent(this,MyServiceReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this,0,TSIntent,PendingIntent.FLAG_CANCEL_CURRENT);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 30 * minutes, pi);
        Log.i(Util.TAG,"Repeating Alarm set up with RTC_WAKEUP");
        
        
		return super.onStartCommand(intent, flags, startId);
	}



	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
