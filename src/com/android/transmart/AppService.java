package com.android.transmart;

import java.text.DateFormat;
import java.util.Date;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class AppService extends IntentService{
	
	public static final String LOCK_NAME_STATIC="com.android.transmart.AppService.Static";
	private static PowerManager.WakeLock lockStatic = null;
	public static final String LOCK_NAME_STATIC_WIFI="com.android.transmart.AppService.StaticWifi";
	private static WifiManager.WifiLock lockStaticWifi = null;
	private static Handler handler=null;
	private String locDetails;


	public AppService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public AppService(){
		super("AppService");
	}
	
	//We Acquire both the Partial and WIFI Wakelock 
	public static void acquireStaticLock(Context context) {
		getLock(context).acquire();
		getWifiLock(context).acquire();
		Log.i(Util.TAG,"AppService : WIFI LOCK and PARTIAL WAKE LOCK SET");
		handler = new Handler();
	}
	
	//Creating Partial Wakelock and returning wakelock context
	synchronized private static PowerManager.WakeLock getLock(Context context) {
		if (lockStatic == null) {
			Log.i(Util.TAG,"AppService : PARTIAL WAKELOCK");
			PowerManager mgr = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
			lockStatic = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOCK_NAME_STATIC);
			lockStatic.setReferenceCounted(true);
		}
		return(lockStatic);
	}
	
	//Creating WIFI Wakelock and returning wakelock context
	synchronized private static WifiManager.WifiLock getWifiLock(Context context) {
		if (lockStaticWifi == null) {
			Log.i(Util.TAG,"AppService : WIFI LOCK");
			WifiManager mgr = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
			lockStaticWifi = mgr.createWifiLock(WifiManager.WIFI_MODE_FULL, LOCK_NAME_STATIC_WIFI);
			lockStaticWifi.setReferenceCounted(true);
		}
		return(lockStaticWifi);
	}

	
	public void currentLocation(Intent intent){
		LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000*30*1,0,locationlistener);
		
		if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            String latitude=String.valueOf(lat);
			String longitude=String.valueOf(lng);
            locDetails = "Lat:" + latitude + "\nLong:" + longitude;
            Log.i(Util.TAG,"on Get Position: "+locDetails);
        }
		
		
		// Define the criteria how to select the locatioin provider -> use
		// default
		//Criteria criteria = new Criteria();
		//provider = locationManager.getBestProvider(criteria, false);
		//Location location = locationManager.getLastKnownLocation(provider);
		
		
			
		
		//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationlistener);
		//locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000*30*1,0,locationlistener);
		// Initialize the location fields
	}
	
	private final LocationListener locationlistener = new LocationListener() {
		
		
		@Override
		public void onLocationChanged(Location location) {
			getPosition(location);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			Toast.makeText(getApplicationContext(), "Enabled new provider " + provider,
					Toast.LENGTH_SHORT).show();
			

		}

		@Override
		public void onProviderDisabled(String provider) {
			Toast.makeText(getApplicationContext(), "Disabled provider " + provider,
					Toast.LENGTH_SHORT).show();
		}
	};
		
		private void getPosition(Location location) {
			// TODO Auto-generated method stub
			 locDetails = "";
		        if (location != null) {
		            double lat = location.getLatitude();
		            double lng = location.getLongitude();
		            String latitude=String.valueOf(lat);
					String longitude=String.valueOf(lng);
		            locDetails = "Lat:" + latitude + "\nLong:" + longitude;
		            Log.i(Util.TAG,"on Get Position: "+locDetails);
		        } else {
		            locDetails = "No location found";
		        }
			/*double lat = location.getLatitude();
			double lng = location.getLongitude();
			String latitude=String.valueOf(lat);
			String longitude=String.valueOf(lng);
			locDetails = latitude+" "+longitude;*/
			
		}
		
	
	
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		try {
			Log.i(Util.TAG,"ALL WORK IS GONNA BE DONE HERE");
			currentLocation(intent);
			String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
			final String Text = currentDateTimeString+" "+locDetails;
			handler.post(new Runnable() {  
				   @Override  
				   public void run() {  
				      Toast.makeText(getApplicationContext(), Text, Toast.LENGTH_SHORT).show();  
				   }  
				}); 
			//Shows a Toast of current time. For test purpose
			
		} finally {
			//Releasing both the wake Locks
			getWifiLock(this).release();
			getLock(this).release();
			Log.i(Util.TAG,"On Handle Intent finally : Locks Released");
		}
		
	}

}
