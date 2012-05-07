package com.android.transmart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class AppService extends IntentService {

	public static final String LOCK_NAME_STATIC = "com.android.transmart.AppService.Static";
	private static PowerManager.WakeLock lockStatic = null;
	public static final String LOCK_NAME_STATIC_WIFI = "com.android.transmart.AppService.StaticWifi";
	private static WifiManager.WifiLock lockStaticWifi = null;
	private static Handler handler = null;
	private String locDetails;
	//private LocationManager locationManager;
	static Location location;
	private long currentTime;
	LocResult res = null;
	

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
		Log.i(Util.TAG, "AppService :In PowerMgr Wake lock");
		LocFinder loc = new LocFinder(context);
		location=loc.currentLocation();
		if (lockStatic == null) {
			Log.i(Util.TAG, "AppService : PARTIAL WAKE LOCK SET");
			
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
		Log.i(Util.TAG, "AppService :In WifiMgr Wifi lock");
		if (lockStaticWifi == null) {
			Log.i(Util.TAG, "AppService : WIFI LOCK SET");
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

	/*public void currentLocation(Intent intent) {
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
	
	*/
	
	protected void doWakefulWork(Intent intent, LocResult res) {
		ConnectivityManager myConnManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		if(myConnManager != null){
			if(myConnManager.getActiveNetworkInfo() != null){
				if(myConnManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI) {
					Log.i(Util.TAG,"appservice: connected to wifi. dowakefulwork");
					Date now = new Date();
					currentTime = System.currentTimeMillis()/1000 - now.getSeconds();
					doMeasurement(myConnManager,res);
					uploadData();
				}
			}
		}
	}

	private void doMeasurement(ConnectivityManager myConnManager, LocResult locRes){
		Log.i(Util.TAG,"appservice: doMeasurement");
		AnyDBAdapter db = new AnyDBAdapter(getApplicationContext());
		try {

			WifiManager myWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
			// get wifi scan results
			List<ScanResult> results = myWifiManager.getScanResults();
			// insert wifi becons results
			db.insertWifiBeaconsRecord(results,locRes, currentTime);
			measureBandwidth(db, myWifiManager,locRes);

		} catch(Exception exc){
			Log.e(Util.TAG,"appservice: measurement couldn't be done.");
			exc.printStackTrace();
		}
		// close db
		db.closeDB();
	}

	private void measureBandwidth(AnyDBAdapter db, WifiManager myWifiManager,LocResult locRes){
		Log.i(Util.TAG,"appservice: connected to wifi. measure bandwidth");
		//get connection information
		HashMap<String, String> wifiHash = new HashMap<String, String>();
		HashMap<String, String> dhcpHash = new HashMap<String, String>();
		WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
		DhcpInfo dhcpinfo = myWifiManager.getDhcpInfo();
		Log.i("DHCP INFO","dhcpinfo : "+dhcpinfo.toString());
		String []splittedDhcpInfo = dhcpinfo.toString().split("\\s+");
		// Form DHCP JSON
		try {
			dhcpHash.put("ip", splittedDhcpInfo[1]);
			dhcpHash.put("gateway", splittedDhcpInfo[3]);
			dhcpHash.put("dns1", splittedDhcpInfo[7]);
			dhcpHash.put("dns2", splittedDhcpInfo[9]);
			dhcpHash.put("leaseDuration", splittedDhcpInfo[14]);
			dhcpHash.put("serverAddress", splittedDhcpInfo[12]);
			dhcpHash.put("netmask", splittedDhcpInfo[5]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Form WIFI JSON
		String wiFiSSID = myWifiInfo.getSSID().replace("'","''");
		wifiHash.put("dhcpinfo", new JSONObject(dhcpHash).toString());
		wifiHash.put("ssid", wiFiSSID);
		wifiHash.put("bssid", myWifiInfo.getBSSID());
		wifiHash.put("mac", myWifiInfo.getMacAddress());
		wifiHash.put("rssi", myWifiInfo.getRssi() + "");
		wifiHash.put("linkspeed", myWifiInfo.getLinkSpeed() + "");
		wifiHash.put("networkid", myWifiInfo.getNetworkId() + "");
		// Json Object for Wifi
		JSONObject jsonDataObject = new JSONObject(wifiHash);
		//do iperf first
		Process process;
		String bandwidth = "", dataTransfered = "";
		try {
			process = Runtime.getRuntime().exec("/data/data/com.android.transmart/iperf -c 23.21.113.175 -p 8468 -u -f K -b 54m -t 10" );
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			int lineCount = 0;
			while ((line = bufferedReader.readLine()) != null){
				lineCount++;
				Log.i(Util.TAG,"appservice: iperf result line: " + line);
				if(lineCount == 8){
					String []splitted = line.split("\\s+");
					if(splitted.length >= 8){
						dataTransfered = splitted[4];
						bandwidth = splitted[6];
					}
				}
			}
			Log.d(Util.TAG, "appservice: bandwidth: " + bandwidth + " data_transfered: " + dataTransfered);
		} catch (IOException e1) {
			Log.e(Util.TAG,"appservice: iperf couldn't be run.");
			e1.printStackTrace();
		}

		//then do our download upload method
		List<Long> downloadSpeedList = new ArrayList<Long>();
		List<Long> uploadSpeedList = new ArrayList<Long>();
		for(int i =0; i<10; i++){
			try {
				downloadSpeedList.add(DownloadFile.execute("http://50.19.247.145/cse622/download.php"));
				uploadSpeedList.add(UploadFile.execute("http://50.19.247.145/cse622/upload.php"));
			} catch(Exception e){
				Log.e("WiFiMonitorService", "List Add Error");
				e.printStackTrace();
			}
		}

		//insert connected record
		db.insertConnectedRecord("wifi", jsonDataObject.toString(), bandwidth, dataTransfered, downloadSpeedList, uploadSpeedList, currentTime,locRes);
	}

	private void uploadData() {
		TelephonyManager tMan = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = tMan.getDeviceId();
		Log.i("AppService",deviceId.toString());
		Log.d(Util.TAG,"appservice: DB Uploading");
		AnyDBAdapter db = new AnyDBAdapter(this);
		// Last Db updated Time
		Log.d(Util.TAG,"appservice: DB Uploading Check");
		// If DB was updated > 1 hour ago
		long lastUpdateInSeconds = db.getLastUpdateTime();
		if (((System.currentTimeMillis()/1000)-lastUpdateInSeconds) > (Util.UPLOAD_INTERVAL_IN_SECONDS)) { // ((System.currentTimeMillis()/1000)-lastUpdateInSeconds) > (60*60) 
			Log.d(Util.TAG,"appservice: DB Uploading-START");
			//upload the data
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httpost = new HttpPost("http://23.21.113.175/transupload.php");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			String response = null;
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			Log.i("Appserv upload","dump : "+db.dumpConnectedDB().toString());
			HashMap<String, JSONArray> map;
			JSONObject jsonObj;
			
			try {
				map = new HashMap<String, JSONArray>();
				map.put("sample", db.dumpConnectedDB());
				jsonObj = new JSONObject(map);
				nameValuePairs.add(new BasicNameValuePair("device_id", deviceId));
				//nameValuePairs.add(new BasicNameValuePair("data_connected", db.dumpConnectedDB().toString()));
				nameValuePairs.add(new BasicNameValuePair("data_connected", jsonObj.toString()));
				
				//nameValuePairs.add(new BasicNameValuePair("data_probe", db.dumpWiFiDB().toString()));
				map = new HashMap<String, JSONArray>();
				map.put("sample", db.dumpWiFiDB());
				jsonObj = new JSONObject(map);
				nameValuePairs.add(new BasicNameValuePair("data_probe", jsonObj.toString()));
				
				
				Date now = new Date();
				nameValuePairs.add(new BasicNameValuePair("timestamp", (System.currentTimeMillis()/1000 - ((now.getMinutes() * 60  + now.getSeconds()))) + ""));
				httpost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				response = httpclient.execute(httpost,responseHandler);
				/*if(response.contains("0")){
					Log.i("AppService","AppService : I returned O");
				}*/
				//JSONObject responseJ = new JSONObject(response);
				//Log.d(Util.TAG,"appservice: Upload Msg " + responseJ.toString());
				Log.d(Util.TAG,"appservice: Upload Msg : " + response);
				
           //if(responseJ.get("err").toString().toLowerCase().contains("no")){
				if(response.contains("OK")){
					Log.i("DBTRUNCATE","AppService : Dropping DB after response : "+response);
					db.truncateWiFiBeaconsData();
					db.truncateConnectedRecord();
					// Update DB with new time
					db.setLastUpdateTime((System.currentTimeMillis()/1000));
				} else {
					Log.d(Util.TAG,"appservice: Database upload couldn't finished successfully.");
					//Log.d(Util.TAG,"appservice: response from server: " + responseJ.get("data").toString());
				}
			}/*catch(JSONException ex){
				Log.w(Util.TAG,"Json Exception thrown");
				ex.printStackTrace();
			}*/
			catch (Exception e) {
				Log.d(Util.TAG,"appservice: " + e.getMessage());
				e.printStackTrace();
			}
			Log.d(Util.TAG,"appservice: DB Uploading-DONE");
		}
		// Close DB
		db.closeDB();
	}

	private LocResult getPosition(Location location) {
		// TODO Auto-generated method stub
		locDetails = "";
		LocResult res = new LocResult();
		
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			float acc=location.getAccuracy();
			double altitude=location.getAltitude();
			String Prov = location.getProvider();
			long locTime = location.getTime();
			
			res.Prov=Prov;
			res.Lat = String.valueOf(lat);
			res.Lng = String.valueOf(lng);
			res.Alt = String.valueOf(altitude);
			res.Acc = String.valueOf(acc);
			res.Time= String.valueOf(locTime);
			Log.i("Loc Time : ",res.Time);
			TransmartService.latitude=res.Lat;
			TransmartService.longitude=res.Lng;
			TransmartService.accuracy = res.Acc;
			TransmartService.altitude=res.Alt;
		    locDetails = "Lat: " + res.Lat + " Long: " + res.Lng+ " Acc: "+res.Acc+" Alt : "+res.Alt+" Prov : "+res.Prov+" locTime : "+res.Time;
		//Log.i(Util.TAG, "on receiver: " + locDetails);
			//locDetails = "Lat:" + latitude + "Long:" + longitude;
			Log.i(Util.TAG, "on Get Position: " + locDetails);
		} else {
			locDetails = "No location found";
		}
		return res;
	}
	

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		try {
			Log.i(Util.TAG, "ALL WORK IS GONNA BE DONE HERE");
			
			//currentLocation(intent);
			LocResult res = getPosition(location);
			doWakefulWork(intent,res);
			
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
			Log.i(Util.TAG, "On Handle Intent finally :Wifi and Partial Wake Locks Released");
			getWifiLock(this).release();
			getLock(this).release();
		}

	}

}
