package com.android.transmart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class AddDownloads extends Activity {
	 
	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.download);
	        Log.i(Util.TAG,"AddDownloads Started");
	        
	      //Get a reference of WebView holder
	    	WebView webview = (WebView) this.findViewById(R.id.browser);
	    	//Get the settings
	    	WebSettings websettings = webview.getSettings();
	    	//Enable Javascript
	    	websettings.setJavaScriptEnabled(true);
	    	//Make the zoom controls visible
	    	websettings.setBuiltInZoomControls(true);
	    	//Load the default url.
	    	webview.loadUrl("http://www.google.com");
	    }
	public void toTransmartHome(View view){
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		Log.i(Util.TAG,"MyService Home Button finishing Activity");
		finish();
	}
	

}
