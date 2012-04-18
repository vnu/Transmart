package com.android.transmart.location;

import android.content.Context;
import android.location.LocationManager;

import com.android.transmart.utils.ILastLocationFinder;
import com.android.transmart.utils.IStrictMode;
import com.android.transmart.utils.LocUpdateRequest;
import com.android.transmart.utils.SharedPref;

/**
 * @author Vnu
 * 
 *         Class to create the correct instances of the different classes with
 *         platform specific implementations.
 * 
 */
public class PlatformSpecific {

	/**
	 * Create a new LastLocationFinder instance
	 * 
	 * @param context Context
	 * @return LastLocationFinder
	 */
	public static ILastLocationFinder getLastLocationFinder(Context context) {
		return LocationConstants.SUPPORTS_GINGERBREAD ? new GBLastLocFinder(
				context) : new LegacyLastLocFinder(context);
	}
	
	/**
	   * Create a new StrictMode instance.
	   * @return StrictMode
	   */
	  public static IStrictMode getStrictMode() {
		if (LocationConstants.SUPPORTS_HONEYCOMB)
	      return new HoneycombStrictMode();
		else if (LocationConstants.SUPPORTS_GINGERBREAD)
	      return new LegacyStrictMode(); 
		else
	      return null;
	  }
	  
	 
	
	 /**
	   * Create a new LocationUpdateRequest
	   * @param locationManager Location Manager
	   * @return LocationUpdateRequest
	   */
	  public static LocUpdateRequest getLocationUpdateRequester(LocationManager locationManager) {
	    return LocationConstants.SUPPORTS_GINGERBREAD ? new GBLocUpdateRequest(locationManager) : new FroyoLocUpdateRequest(locationManager);    
	  }
	  
	  /**
	   * Create a new SharedPref
	   * @param context Context
	   * @return SharedPreferenceSaver
	   */
	  public static SharedPref getSharedPreferenceSaver(Context context) {
	    return  LocationConstants.SUPPORTS_GINGERBREAD ? 
	       new GBSharedPref(context) : 
	       LocationConstants.SUPPORTS_FROYO ? 
	           new FroyoSharedPref(context) :
	           new LegacySharedPref(context);
	  }

}
