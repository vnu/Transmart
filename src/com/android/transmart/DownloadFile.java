package com.android.transmart;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class DownloadFile{
	protected static Long execute(String urlComing) {
		long mStartTime = 0;
		long mTakenTime = 0;
        try {
            URL url = new URL(urlComing);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            InputStream input = urlConnection.getInputStream();
            mStartTime = System.currentTimeMillis();
            int totalSize = urlConnection.getContentLength();
            int downloadedSize = 0;
            int count = 0;
            byte data[] = new byte[1024];
            
            while ((count = input.read(data)) > 0) {
            	downloadedSize += count;
            }
            
            mTakenTime = System.currentTimeMillis() - mStartTime;
            Log.d("download", "time taken =>" + mTakenTime);
            Log.d("download", "response =>" + " {contentlength: " + totalSize + " , Downloaded:" + downloadedSize + "}");
            input.close();
        } catch (Exception e) { 
        	e.printStackTrace();
        }
		return mTakenTime;
	}
}
