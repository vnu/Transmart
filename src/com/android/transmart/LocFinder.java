package com.android.transmart;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

public class LocFinder {
	private String locDetails;
	private LocationManager locationManager;
	Location location;
	
	public LocFinder(Context context){
		Log.i("In LocFinder","In constructor setting connection");
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		

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
		
				
		// Initialize the location fields

	}
	
	public Location currentLocation(){
		Log.i("LocFiner","Finding Location");
		locationManager.removeUpdates(locationlistener);
			return location;
		
		
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
			

		}

		@Override
		public void onProviderDisabled(String provider) {
			

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

}
