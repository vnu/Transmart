package com.android.transmart.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.android.transmart.location.LegacyLastLocFinder;
import com.android.transmart.location.LocationConstants;
import com.android.transmart.services.EclairPlacesUpdateService;
import com.android.transmart.services.PlacesUpdateService;
/**
 * This Receiver class is used to listen for Broadcast Intents that announce
 * that a location change has occurred while this application isn't visible.
 * 
 * Where possible, this is triggered by a Passive Location listener.
 */
public class PassiveLocReceiver extends BroadcastReceiver {
	
	protected static String TAG = "PassiveLocReceiver";
	
	

	@Override
	public void onReceive(Context context, Intent intent) {
		String key = LocationManager.KEY_LOCATION_CHANGED;
		Location location = null;
		if (intent.hasExtra(key)) {
		      // This update came from Passive provider, so we can extract the location
		      // directly.
		      location = (Location)intent.getExtras().get(key);      
		    }
		else {
			// This update came from a recurring alarm. We need to determine if there
		      // has been a more recent Location received than the last location we used.
		      
		      // Get the best last location detected from the providers.
			LegacyLastLocFinder lastlocfinder = new LegacyLastLocFinder(context);
			location = lastlocfinder.getLastBestLocation(LocationConstants.MAX_DISTANCE,System.currentTimeMillis()-LocationConstants.MAX_TIME);
			SharedPreferences prefs = context.getSharedPreferences(LocationConstants.SHARED_PREFERENCE_FILE,Context.MODE_PRIVATE);
			

		      // Get the last location we used to get a listing.
		      long lastTime = prefs.getLong(LocationConstants.SP_KEY_LAST_LIST_UPDATE_TIME, Long.MIN_VALUE);
		      long lastLat = prefs.getLong(LocationConstants.SP_KEY_LAST_LIST_UPDATE_LAT, Long.MIN_VALUE);
		      long lastLng = prefs.getLong(LocationConstants.SP_KEY_LAST_LIST_UPDATE_LNG, Long.MIN_VALUE);
		      Location lastLocation = new Location(LocationConstants.CONSTRUCTED_LOCATION_PROVIDER);
		      lastLocation.setLatitude(lastLat);
		      lastLocation.setLongitude(lastLng);
		      
		      // Check if the last location detected from the providers is either too soon, or too close to the last
		      // value we used. If it is within those thresholds we set the location to null to prevent the update
		      // Service being run unnecessarily (and spending battery on data transfers).
		      if ((lastTime > System.currentTimeMillis()-LocationConstants.MAX_TIME) ||
		         (lastLocation.distanceTo(location) < LocationConstants.MAX_DISTANCE))
		        location = null;
		      
		      
		      if (location != null) {
		          Log.d(TAG, "Passivly updating place list.");
		          Intent updateServiceIntent = new Intent(context, LocationConstants.SUPPORTS_ECLAIR ? EclairPlacesUpdateService.class : PlacesUpdateService.class);
		          //Intent updateServiceIntent = new Intent(context, PlacesUpdateService.class);
		          updateServiceIntent.putExtra(LocationConstants.EXTRA_KEY_LOCATION, location);
		          updateServiceIntent.putExtra(LocationConstants.EXTRA_KEY_RADIUS, LocationConstants.DEFAULT_RADIUS);
		          updateServiceIntent.putExtra(LocationConstants.EXTRA_KEY_FORCEREFRESH, false);
		          context.startService(updateServiceIntent);   
		        }
			
		}
	}

}
