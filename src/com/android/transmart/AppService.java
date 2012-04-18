package com.android.transmart;

import java.text.DateFormat;
import java.util.Date;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class AppService extends IntentService {

	public static final String LOCK_NAME_STATIC = "com.android.transmart.AppService.Static";
	private static PowerManager.WakeLock lockStatic = null;
	public static final String LOCK_NAME_STATIC_WIFI = "com.android.transmart.AppService.StaticWifi";
	private static WifiManager.WifiLock lockStaticWifi = null;
	private static Handler handler = null;
	private String locDetails;
	private LocationManager locationManager;
	

	public AppService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public AppService() {
		super("AppService");
	}

	// We Acquire both the Partial and WIFI Wakelock
	public static void acquireStaticLock(Context context) {
		getLock(context).acquire();
		getWifiLock(context).acquire();
		Log.i(Util.TAG, "AppService : WIFI LOCK and PARTIAL WAKE LOCK SET");
		handler = new Handler();
	}
		 

	// Creating Partial Wakelock and returning wakelock context
	/**
	 * Gets the context, creates a Partial Wake lock and returns the wake lock
	 * 
	 * @param context
	 * @return PowerManager.WakeLock
	 */
	synchronized private static PowerManager.WakeLock getLock(Context context) {
		if (lockStatic == null) {
			Log.i(Util.TAG, "AppService : PARTIAL WAKELOCK");
			
			PowerManager mgr = (PowerManager) context
					.getSystemService(Context.POWER_SERVICE);
			lockStatic = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
					LOCK_NAME_STATIC);
			lockStatic.setReferenceCounted(true);
		}
		return (lockStatic);
	}

	
	/**
	 *  
	 * Gets the context, creates a WIFI Wake lock and returns the wake lock
	 *  
	 * @param context
	 * @return WifiManager.WifiLock
	 */
	synchronized private static WifiManager.WifiLock getWifiLock(Context context) {
		if (lockStaticWifi == null) {
			Log.i(Util.TAG, "AppService : WIFI LOCK");
			WifiManager mgr = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			lockStaticWifi = mgr.createWifiLock(WifiManager.WIFI_MODE_FULL,
					LOCK_NAME_STATIC_WIFI);
			lockStaticWifi.setReferenceCounted(true);
		}
		return (lockStaticWifi);
	}
	/*public void currentLocation(Intent intent) {
		GBLastLocFinder lastloc = new GBLastLocFinder(getBaseContext());
		Location location=lastloc.getLastBestLocation(0, 1000*30);
		double lat = location.getLatitude();
		double lng = location.getLongitude();
		float acc=location.getAccuracy();
		String latitude = String.valueOf(lat);
		String longitude = String.valueOf(lng);
		String accur = String.valueOf(acc);
		locDetails = "Lat: " + latitude + " Long: " + longitude+ " Accuracy: "+accur;
		Log.i(Util.TAG, "on receiver: " + locDetails);
	}*/

	public void currentLocation(Intent intent) {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		Location location;

		if (mWifi.isConnected()) {
			
			location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0, locationlistener);
			Log.i(Util.TAG,"In Current Location, using Network");
		    // Do whatever
		}
		else{
			location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, locationlistener);
			Log.i(Util.TAG,"In Current Location, using GPS");
		}
		
		
		getPosition(location);
		locationManager.removeUpdates(locationlistener);

		
		// Initialize the location fields

	}

	private LocationListener locationlistener = new LocationListener() {

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
			Toast.makeText(getApplicationContext(),
					"Enabled new provider " + provider, Toast.LENGTH_SHORT)
					.show();

		}

		@Override
		public void onProviderDisabled(String provider) {
			Toast.makeText(getApplicationContext(),
					"Disabled provider " + provider, Toast.LENGTH_SHORT).show();

		}

	};

	private void getPosition(Location location) {
		// TODO Auto-generated method stub
		locDetails = "";
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			String latitude = String.valueOf(lat);
			String longitude = String.valueOf(lng);
			float acc=location.getAccuracy();
			double altitude=location.getAltitude();
			String alt = String.valueOf(altitude);
			String accur = String.valueOf(acc);
			TransmartService.latitude=latitude;
			TransmartService.longitude=longitude;
			TransmartService.accuracy = accur;
			TransmartService.altitude=alt;
		locDetails = "Lat: " + latitude + " Long: " + longitude+ " Accuracy: "+accur;
		//Log.i(Util.TAG, "on receiver: " + locDetails);
			//locDetails = "Lat:" + latitude + "Long:" + longitude;
			Log.i(Util.TAG, "on Get Position: " + locDetails);
		} else {
			locDetails = "No location found";
		}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		try {
			Log.i(Util.TAG, "ALL WORK IS GONNA BE DONE HERE");
			currentLocation(intent);
			
			String currentDateTimeString = DateFormat.getDateTimeInstance()
					.format(new Date());
			final String Text = currentDateTimeString + " " + locDetails;
			Log.i(Util.TAG, "On Handle Intent:" + Text);

			//Creating a thread to run the toast
			handler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), Text,
							Toast.LENGTH_LONG).show();
				}
			});
			// Shows a Toast of current time. For test purpose

		} finally {
			// Releasing both the wake Locks
			// locationManager.removeUpdates(locationlistener);
			Log.i(Util.TAG, "On Handle Intent finally : Locks Released");
			getWifiLock(this).release();
			getLock(this).release();
		}

	}

}
