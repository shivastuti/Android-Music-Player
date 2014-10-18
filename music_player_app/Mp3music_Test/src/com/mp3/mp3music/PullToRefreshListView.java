package com.mp3.mp3music;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug.ExportedProperty;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class PullToRefreshListView extends ListActivity {

	// Songs list
		public ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.playlist);

			ArrayList<HashMap<String, String>> songsListData = new ArrayList<HashMap<String, String>>();

			SongsManager plm = new SongsManager();
			// get all songs from sdcard
			this.songsList = plm.getPlayList();

			// looping through playlist
			for (int i = 0; i < songsList.size(); i++) {
				// creating new HashMap
				HashMap<String, String> song = songsList.get(i);

				// adding HashList to ArrayList
				songsListData.add(song);
			}

			// Adding menuItems to ListView
			ListAdapter adapter = new SimpleAdapter(this, songsListData,
					R.layout.playlist_item, new String[] { "songTitle" }, new int[] {
							R.id.songTitle });

			setListAdapter(adapter);

			// selecting single ListView item
			ListView lv = getListView();
			// listening to single listitem click
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// getting listitem index
					int songIndex = position;
					
					// Starting new intent
//					Intent in = new Intent(getApplicationContext(),
//							AndroidBuildingMusicPlayerActivity.class);
//					// Sending songIndex to PlayerActivity
//					in.putExtra("songIndex", songIndex);
//					setResult(100, in);
//					// Closing PlayListView
//					finish();
				}
			});

		}

	

}
