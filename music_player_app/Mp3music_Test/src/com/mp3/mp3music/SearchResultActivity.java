package com.mp3.mp3music;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import com.http.HttpWrapper;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SearchResultActivity extends Activity {
	ListView list;
    ResultListAdapter adapter;

	static final String KEY_TITLE = "title";
	static final String KEY_TIME = "time";
	static final String KEY_SIZE = "size";
	static final String KEY_LINK = "link";
	
	JSONObject json_Data;
	
	TextView txtCount;
	ArrayList<HashMap<String, String>> songsList;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_result_view4);
		
		txtCount = (TextView) findViewById(R.id.textView1);
		
		Intent intent = getIntent();
        // Receiving the Data
        HttpWrapper.search_key = intent.getStringExtra("search_key");
        json_Data = HttpWrapper.getData();
        
        if(json_Data == null) {
        	Intent nextScreen = new Intent(SearchResultActivity.this, SearchActivity.class);
        	startActivity(nextScreen);
        	
        	this.finish();
        }
        
        songsList = new ArrayList<HashMap<String, String>>();
		
		txtCount.setText("FOUND " + String.valueOf(json_Data.length() - 3) + " SONG");
		
		// looping through all song nodes <song>
		for (int i = 0; i < json_Data.length() - 3; i++) {
			
			try {
                JSONObject c = json_Data.getJSONObject(String.valueOf(i).toString());
                
                // creating new HashMap
    			HashMap<String, String> map = new HashMap<String, String>();
    			// adding each child node to HashMap key => value
    			map.put(KEY_TITLE, c.get("title").toString());
    			float time = Float.valueOf(c.get("time").toString()) / 60;
    			String strTime = String.format("%.2f", time);
    			strTime = strTime.replace(".", ":");
    			map.put(KEY_TIME, strTime);
    			float size = Float.valueOf(c.get("size").toString()) / 1048576;
    			map.put(KEY_SIZE, String.format("%.2f", size));
    			map.put(KEY_LINK, c.get("link").toString());
    			// adding HashList to ArrayList
    			songsList.add(map);            
    		}
            catch (JSONException e) {
                e.printStackTrace();
            }
		}
        list = (ListView) findViewById(R.id.listView1);
        
        // Getting adapter by passing xml data ArrayList
        adapter = new ResultListAdapter(this, songsList);
        list.setAdapter(adapter);
        
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				
				
				TextView item_text = (TextView) v.findViewById(R.id.item_text);
			    TextView item_infos = (TextView) v.findViewById(R.id.item_infos);
			    
			    Intent nextScreen = new Intent(getApplicationContext(), SongView.class);
				 
                //Sending data to another Activity
                nextScreen.putExtra("item_text", item_text.getText().toString());
                nextScreen.putExtra("item_infos", item_infos.getText().toString());
                nextScreen.putExtra("item_link", songsList.get(position).get(KEY_LINK).toString());
                
                startActivity(nextScreen);
			}
        	
        });
	}
}
