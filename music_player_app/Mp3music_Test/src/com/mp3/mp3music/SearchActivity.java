package com.mp3.mp3music;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.http.HttpWrapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SearchActivity extends Activity {
	
	Button search;
	Button clear;
	EditText txtSearch;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		//Prepare Eula
		new SimpleEula(this).show();
		
		txtSearch = (EditText) findViewById(R.id.editText1);
		txtSearch.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
			            (keyCode == KeyEvent.KEYCODE_ENTER)) {
					// Perform action on key press
					if(!txtSearch.getText().toString().equals("")) {
						if(txtSearch.getText().toString().equals(",,")) { return false; }
						searchResult();
					}
					
					return true;
		        }
				return false;
			}			
		});
		
		search = (Button) findViewById(R.id.button1);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!txtSearch.getText().toString().equals("")) {
					if(txtSearch.getText().toString().equals(",,")) { return; }
					searchResult();
				}
			}			
		});
		
		clear = (Button) findViewById(R.id.clear_input);
		clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				txtSearch.setText("");
				txtSearch.clearFocus();
			}			
		});			
	}
	
	public void searchResult() {
		Intent nextScreen = new Intent(getApplicationContext(), SearchResultActivity.class);
		 
        //Sending data to another Activity
		try {
			String query = URLEncoder.encode(txtSearch.getText().toString(), "utf-8");
			
			nextScreen.putExtra("search_key", query);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
		
		boolean haveConnectedWifi = false;
	    boolean haveConnectedMobile = false;

	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo) {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	            if (ni.isConnected())
	                haveConnectedWifi = true;
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	            if (ni.isConnected())
	                haveConnectedMobile = true;
	    }
	    
	    if(haveConnectedWifi) {
	    	startActivity(nextScreen);
	    } else if(haveConnectedMobile) {
	    	startActivity(nextScreen);
	    } else {
	    	alert(0);
	    }
	    
//		final ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//		final android.net.NetworkInfo wifi =  connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//		
//		final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//		
//		if( wifi.isAvailable() ){
//			startActivity(nextScreen);
//		}
//		else if( mobile.isAvailable() ){
//			startActivity(nextScreen);
//		}
	}	
	
	private void alert(int idx) {
		AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(
				SearchActivity.this);
		
		String alertTitle = "";
		String alertMessage = "";
		switch(idx) {
			case 0:
				alertTitle = "Internet Problem";
				alertMessage = "No internet Available please connect with wifi, 3g, 4g or lte to Search Music online";
				break;
			case 1:
				alertTitle = "No Result";
				alertMessage = "Search result no";
				break;
		}
		// Setting Dialog Title
		alertDialog1.setTitle(alertTitle);

		// Setting Dialog Message
		alertDialog1.setMessage(alertMessage);

		// Setting Icon to Dialog
		alertDialog1.setIcon(R.drawable.tick);
		
		// Setting Positive "Yes" Btn
		alertDialog1.setPositiveButton("YES",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Write your code here to execute after dialog
					}
				});
		
		alertDialog1.show();
	}
}
