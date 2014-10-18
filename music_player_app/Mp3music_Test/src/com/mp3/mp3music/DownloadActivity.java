package com.mp3.mp3music;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;

import com.util.Contact;
import com.util.DatabaseHandler;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

public class DownloadActivity extends Activity {
	ListView list;
    DownloadListAdapter adapter;
    
	DatabaseHandler db;
	public static MediaPlayer down_mp;

	ArrayList<HashMap<String, String>> songsListData;
	
//	final String MEDIA_PATH = Environment.getExternalStorageDirectory().getPath() + "/";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_downloads);
		
		db = new DatabaseHandler(this);
		List<Contact> songsList = db.getAllContacts();		
		
//		down_mp = new MediaPlayer();

		songsListData = new ArrayList<HashMap<String, String>>();
		
		for (int i = 0; i < songsList.size(); i++) {
			// creating new HashMap
			
			HashMap<String, String> map = new HashMap<String, String>();
			// adding each child node to HashMap key => value
			map.put("songTitle", songsList.get(i).getName());
			map.put("songPath", songsList.get(i).getFilename());
			
			// adding HashList to ArrayList
			songsListData.add(map);
		}

		list=(ListView)findViewById(R.id.listView1);
		
		// Getting adapter by passing xml data ArrayList
		adapter=new DownloadListAdapter(this, songsListData);        
        list.setAdapter(adapter);
        
//        list.setOnItemClickListener(new OnItemClickListener() {
//        	@Override
//			public void onItemClick(AdapterView<?> arg0, View v, int position,
//					long id) {
//				playSong(position);
//        	}
//        });
	}
	
//	public void  playSong(int songIndex){
//		// Play song
//		try {
//			if(MusicActivity.mp != null) MusicActivity.mp.stop();
//			
//        	down_mp.reset();
//			down_mp.setDataSource(MEDIA_PATH + songsListData.get(songIndex).get("songTitle") + ".mp3");
//			down_mp.prepare();
//			down_mp.start();
//			// Displaying Song title
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (IllegalStateException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
	}

}
