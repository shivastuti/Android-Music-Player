package com.mp3.mp3music;


import android.os.Bundle;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Menu;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs_main);
		
		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Reusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab
		
		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, SearchActivity.class);
		spec = tabHost.newTabSpec("Search")
				.setIndicator("SEARCH", res.getDrawable(R.drawable.section_header))
				.setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs

		intent = new Intent().setClass(this, MusicActivity.class);
		spec = tabHost.newTabSpec("Music")
				.setIndicator("MUSIC", res.getDrawable(R.drawable.section_header))
				.setContent(intent);
		tabHost.addTab(spec);
				 
		intent = new Intent().setClass(this, DownloadActivity.class);
		spec = tabHost
				.newTabSpec("Downloads")
				.setIndicator("DOWNLOADS",
						res.getDrawable(R.drawable.section_header))
				.setContent(intent);
		tabHost.addTab(spec);
		
		//set tab which one you want open first time 0 or 1 or 2
		tabHost.setCurrentTab(0);
	}
}
