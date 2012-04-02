package com.android.transmart;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.util.Log;

public class AppService extends IntentService{
	
	public static final String LOCK_NAME_STATIC="com.android.transmart.AppService.Static";
	private static PowerManager.WakeLock lockStatic = null;
	public static final String LOCK_NAME_STATIC_WIFI="com.android.transmart.AppService.StaticWifi";
	private static WifiManager.WifiLock lockStaticWifi = null;


	public AppService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public AppService(){
		super("AppService");
	}
	
	public static void acquireStaticLock(Context context) {
		getLock(context).acquire();
		getWifiLock(context).acquire();
		Log.i(Util.TAG,"AppService : WIFI LOCK and PARTIAL WAKE LOCK SET");
	}
	
	synchronized private static PowerManager.WakeLock getLock(Context context) {
		if (lockStatic == null) {
			Log.i(Util.TAG,"AppService : PARTIAL WAKELOCK");
			PowerManager mgr = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
			lockStatic = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOCK_NAME_STATIC);
			lockStatic.setReferenceCounted(true);
		}
		return(lockStatic);
	}
	
	synchronized private static WifiManager.WifiLock getWifiLock(Context context) {
		if (lockStaticWifi == null) {
			Log.i(Util.TAG,"AppService : WIFI LOCK");
			WifiManager mgr = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
			lockStaticWifi = mgr.createWifiLock(WifiManager.WIFI_MODE_FULL, LOCK_NAME_STATIC_WIFI);
			lockStaticWifi.setReferenceCounted(true);
		}
		return(lockStaticWifi);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		try {
			Log.i(Util.TAG,"ALL WORK IS GONNA BE DONE HERE");
		} finally {
			getWifiLock(this).release();
			getLock(this).release();
			Log.i(Util.TAG,"On Handle Intent finally : Locks Released");
		}
		
	}

}
