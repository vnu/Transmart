package com.android.transmart.location;

import android.app.PendingIntent;
import android.location.LocationManager;

import com.android.transmart.utils.LocUpdateRequest;

public class FroyoLocUpdateRequest extends LocUpdateRequest {

	protected FroyoLocUpdateRequest(LocationManager locationManager) {
		super(locationManager);
	}
	
	@Override
	  public void requestPassiveLocationUpdates(long minTime, long minDistance, PendingIntent pendingIntent) {
	    // Froyo introduced the Passive Location Provider, which receives updates whenever a 3rd party app 
	    // receives location updates.
	    locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, LocationConstants.MAX_TIME, LocationConstants.MAX_DISTANCE, pendingIntent);    
	  }

}
