package com.android.transmart;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class TransmartActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.i(Util.TAG,"TransmartActivity Started");
    }
    
    //Button onClick method for service button
    public void myService(View view){
    	Intent myIntent = new Intent(view.getContext(), MyService.class);
        startActivityForResult(myIntent, 0);
    }
    
    public void addDownload(View view){
    	Intent myIntent = new Intent(view.getContext(), AddDownloads.class);
        startActivityForResult(myIntent, 0);
    	
    }
}