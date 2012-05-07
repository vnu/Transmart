package com.android.transmart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.ScanResult;
import android.util.Log;

public class AnyDBAdapter {

	private static final String TAG = "AnyDBAdapter";
    private SQLiteDatabase mDb;
    private static final String DATABASE_NAME = "db";
    private static final String CONNECTED = "connected";
    private static final String WIFI = "wifi";

    public AnyDBAdapter(Context context) {
        mDb = context.openOrCreateDatabase(DATABASE_NAME, 0, null);
        
        // Connected Table
        mDb.execSQL("CREATE TABLE IF NOT EXISTS " +
        		CONNECTED +
				" (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        		" type TEXT," +
				" data TEXT," + 
        		" bandwidth TEXT," +
        		" data_transfered TEXT," +
        		" download_speed TEXT," +
        		" upload_speed TEXT," +
				" timestamp BIGINT,"+
        		" latitude TEXT, longitude TEXT, accuracy TEXT, altitude TEXT, provider TEXT, loctime TEXT);");
        
        // Connected Wifi Beacons Table
        mDb.execSQL("CREATE TABLE IF NOT EXISTS " +
				WIFI +
				" (_id INTEGER PRIMARY KEY AUTOINCREMENT, ssid TEXT," +
				" bssid TEXT, capabilities TEXT," +				 
				" level TEXT, frequency TEXT, timestamp BIGINT," +
				" latitude TEXT, longitude TEXT, accuracy TEXT, altitude TEXT, provider TEXT, loctime TEXT);");
       
        // Last updated Time
        mDb.execSQL("CREATE TABLE IF NOT EXISTS" +
				" uploadTime" +
				" (_id INTEGER PRIMARY KEY AUTOINCREMENT, time BIGINT);");
        
    }
    
    public Cursor getConnectedRecord() {
    	String command = "SELECT * FROM " + CONNECTED + " ORDER BY _id DESC";
    	return mDb.rawQuery(command, new String[]{});
    }
    
    public Cursor getWifiBeconsRecords() {
    	String command = "SELECT * FROM " + WIFI + " ORDER BY _id DESC";
    	return mDb.rawQuery(command, new String[]{});
    }
    
    public Cursor getIperfData() {
    	String command = "SELECT * FROM iperf ORDER BY _id DESC";
    	return mDb.rawQuery(command, null);
    }
    
    public long getLastUpdateTime() {
    	String command = "SELECT time FROM uploadTime ORDER BY time DESC LIMIT 0,1";
    	Cursor c = mDb.rawQuery(command, null);
    	long res = 0L;
    	if (c != null ) {
			if  (c.moveToFirst()) {
				res = c.getLong(c.getColumnIndex("time"));
			}
    	}
    	return res;
    }
    
    public void setLastUpdateTime(long time) {
    	String command = "INSERT OR REPLACE INTO uploadTime (_id,time) VALUES (1,"+ time +")";
    	mDb.execSQL(command);
    }
    
    /*
     * Connected Record
     */
    public void insertConnectedRecord(String type, String data, String bandwidth, String size, List<Long> download_speed, List<Long> upload_speed, long timestamp,LocResult locRes) {
        String command = 
      		  "INSERT INTO " + CONNECTED + " values ("
      		  + "null,'"
      		  + type + "','"
      		  + data + "','"
      		  + bandwidth + "','"
      		  + size + "',"
      		  + getAverage(download_speed) + ","
      		  + getAverage(upload_speed) + ","
      		  + timestamp + ",'"
     		  + locRes.Lat + "','"
     		  + locRes.Lng + "','"
     		  + locRes.Acc + "','"
     		  + locRes.Alt + "','"
     		  + locRes.Prov + "','"
     		  + locRes.Time + "'"
     		  + ");";
        
        mDb.execSQL(command);
        Log.d(TAG, command);
    }    
    
    
	public void truncateConnectedRecord(){
		String command = "DROP TABLE IF EXISTS " + CONNECTED;
		mDb.execSQL(command);
	}


    public void insertWifiBeaconsRecord(List<ScanResult> results, LocResult locRes, long timestamp) {
    	//CREATE TABLE wifi (id integer PRIMARY KEY AUTOINCREMENT,ssid text,bssid text,capabilities text,level text,frequency text,"timestamp" datetime)
    	for (ScanResult config: results) {
    		String ssid = config.SSID.replace("'","''");
	        String command = 
	      		  "INSERT INTO " + WIFI + " values ("
	      		  + "null,'"
	      		  + ssid + "','"
	      		  + config.BSSID + "','"
	      		  + config.capabilities + "','"
	      		  + config.level + "','"
	      		  + config.frequency + "',"
	      		  + timestamp + ",'"
	      		  + locRes.Lat + "','"
	      		  + locRes.Lng + "','"
	      		  + locRes.Acc + "','"
	      		  + locRes.Alt + "','"
	      		  + locRes.Prov + "','"
	      		  + locRes.Time+"');";
	        Log.d(TAG, command);
	        mDb.execSQL(command);
    	}
    }
    
    public void truncateWiFiBeaconsData(){
    	//instead of truncate we drop table because drop&create is more efficient than truncate
    	//and each time we create AnyDBAdapter instance, we already check if table exists or not and handle
    	String command = "DROP TABLE IF EXISTS wifi";
    	mDb.execSQL(command);
    }
    
    public JSONArray dumpConnectedDB(){
    	List<JSONObject> wifiConnecteds = new ArrayList<JSONObject>();
    	Cursor c = getConnectedRecord();
    	if(c != null ){
			if (c.moveToFirst()){
				do {
					HashMap<String, String> connected = new HashMap<String, String>();
					connected.put("id", c.getString(c.getColumnIndex("_id")));
					connected.put("type", c.getString(c.getColumnIndex("type")));
					connected.put("data", c.getString(c.getColumnIndex("data")));
					connected.put("bandwidth", c.getString(c.getColumnIndex("bandwidth")));
					connected.put("data_transfered", c.getString(c.getColumnIndex("data_transfered")));
					connected.put("upload_speed", c.getString(c.getColumnIndex("upload_speed")));
					connected.put("download_speed", c.getString(c.getColumnIndex("download_speed")));
					connected.put("timestamp", c.getString(c.getColumnIndex("timestamp")));
					connected.put("latitude", c.getString(c.getColumnIndex("latitude")));
					connected.put("longitude", c.getString(c.getColumnIndex("longitude")));
					connected.put("accuracy", c.getString(c.getColumnIndex("accuracy")));
					connected.put("altitude", c.getString(c.getColumnIndex("altitude")));
					connected.put("provider", c.getString(c.getColumnIndex("provider")));
					connected.put("loctime", c.getString(c.getColumnIndex("loctime")));
					JSONObject object = new JSONObject(connected);
					wifiConnecteds.add(object);
				} while(c.moveToNext());
			}
			JSONArray jsonArray = new JSONArray(wifiConnecteds);
			return jsonArray;
		}
    	return null;
    }
    
    public JSONArray dumpWiFiDB(){
    	Cursor c = getWifiBeconsRecords();
    	List<JSONObject> wifiBecons = new ArrayList<JSONObject>();
    	if(c != null ){
			if (c.moveToFirst()){
				do {
					String ssid = c.getString(c.getColumnIndex("ssid")).replace("'","''");
					HashMap<String, String> wifiBeacon = new HashMap<String, String>();
					wifiBeacon.put("id", c.getString(c.getColumnIndex("_id")));
					wifiBeacon.put("ssid", ssid);
					wifiBeacon.put("bssid", c.getString(c.getColumnIndex("bssid")));
					wifiBeacon.put("capabilities", c.getString(c.getColumnIndex("capabilities")));
					wifiBeacon.put("level", c.getString(c.getColumnIndex("level")));
					wifiBeacon.put("frequency", c.getString(c.getColumnIndex("frequency")));
					wifiBeacon.put("timestamp", c.getString(c.getColumnIndex("timestamp")));
					wifiBeacon.put("latitude", c.getString(c.getColumnIndex("latitude")));
					wifiBeacon.put("longitude", c.getString(c.getColumnIndex("longitude")));
					wifiBeacon.put("accuracy", c.getString(c.getColumnIndex("accuracy")));
					wifiBeacon.put("altitude", c.getString(c.getColumnIndex("altitude")));
					wifiBeacon.put("provider", c.getString(c.getColumnIndex("provider")));
					wifiBeacon.put("loctime", c.getString(c.getColumnIndex("loctime")));
					JSONObject object = new JSONObject(wifiBeacon);
					wifiBecons.add(object);
				} while(c.moveToNext());
			}
			JSONArray jsonArray = new JSONArray(wifiBecons);
			Log.i("ANYDB","JSON WIFIDUMP : "+jsonArray.toString());
			return jsonArray;
		}
    	return null;
    }
    
    public void closeDB(){
    	mDb.close();
    }
    
    private long getAverage(List<Long> list) {
    	long avg = 100;
    	for (long i: list) {
    		avg += i;
    	}
    	avg = avg/list.size();
    	return avg;
    }
}