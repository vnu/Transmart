package com.android.transmart;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;

import com.android.transmart.utils.ILastLocationFinder;
import com.android.transmart.utils.LocUpdateRequest;
import com.android.transmart.utils.SharedPref;

public class LocaleActivity extends FragmentActivity {

	protected static String TAG = "LocaleActivity";

	protected PackageManager packageManager;
	protected NotificationManager notificationManager;
	protected LocationManager locationManager;

	protected SharedPreferences prefs;
	protected Editor prefsEditor;
	protected SharedPref sharedPreferenceSaver;

	protected Criteria criteria;
	protected ILastLocationFinder lastLocationFinder;
	protected LocUpdateRequest locationUpdateRequester;
	protected PendingIntent locationListenerPendingIntent;
	protected PendingIntent locationListenerPassivePendingIntent;
	
	
	
	
	
	/*protected PlaceListFragment placeListFragment;
	protected CheckinFragment checkinFragment;
	protected PlaceDetailFragment placeDetailFragment;

	protected IntentFilter newCheckinFilter;
	protected ComponentName newCheckinReceiverName;*/

}
