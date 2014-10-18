package com.mp3.mp3music;

import java.io.IOException;
import java.net.MalformedURLException;


import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MusicActivity extends Activity implements OnClickListener, OnCompletionListener, SeekBar.OnSeekBarChangeListener {
	
	ImageButton btnPrevious, btnBackward, btnPlay, btnForward, btnNext, btnRepeat, btnShuffle;
	SeekBar songProgressBar;
	
	ListView listView;
	SimpleListAdapter adapter;
	
	private TextView songTitleLabel;
	private TextView songCurrentDurationLabel;
	private TextView songTotalDurationLabel;

	boolean m_bPlay = true;
	private Handler mHandler = new Handler();
	private SongsManager songManager;
	private Utilities utils;
	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> songsListData;
	public static MediaPlayer mp;
	
	private int seekForwardTime = 5000; // 5000 milliseconds
	private int seekBackwardTime = 5000; // 5000 milliseconds
	private int currentSongIndex = 0; 
	
	private boolean isShuffle = false;
	private boolean isRepeat = false;
	
	private RetrieveSearch retrievesearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.player);
		
		mp = new MediaPlayer();
		songManager = new SongsManager();
		utils = new Utilities();

		songsListData = new ArrayList<HashMap<String, String>>();
		
		btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
		btnPrevious.setOnClickListener(this);
		btnBackward = (ImageButton) findViewById(R.id.btnBackward);
		btnBackward.setOnClickListener(this);
		btnPlay = (ImageButton) findViewById(R.id.btnPlay);
		btnPlay.setOnClickListener(this);
		btnForward = (ImageButton) findViewById(R.id.btnForward);
		btnForward.setOnClickListener(this);
		btnNext = (ImageButton) findViewById(R.id.btnNext);
		btnNext.setOnClickListener(this);
		btnRepeat = (ImageButton) findViewById(R.id.btnRepeat);
		btnRepeat.setOnClickListener(this);
		btnShuffle = (ImageButton) findViewById(R.id.btnShuffle);
		btnShuffle.setOnClickListener(this);
		
		songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
		songTitleLabel = (TextView) findViewById(R.id.textView1);
		songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
		songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
		
		listView = (ListView) findViewById(R.id.listView1);
		
		songProgressBar.setOnSeekBarChangeListener(this); // Important
		mp.setOnCompletionListener(this); // Important

		// Getting all songs list
		
		retrievesearch = new RetrieveSearch();
		retrievesearch.execute();
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting listitem index
				currentSongIndex = position; 
				playSong(currentSongIndex);
			}
		});
	}
	
	private class RetrieveSearch extends AsyncTask<Void, Void, Void> {
		private ProgressDialog cancelDialog = null;

		@Override
		protected Void doInBackground(Void... params) {

			try {
				getData();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (StringIndexOutOfBoundsException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();

			try {
				cancelDialog.dismiss();
				cancelDialog = null;
			} catch (Exception e) {

			}
		}

		@Override
		protected void onPreExecute() {
			cancelDialog = new ProgressDialog(MusicActivity.this);
			cancelDialog.setMessage("Please wait...");
			cancelDialog.show();

			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {

			listView.setAdapter(adapter);

			try {
				cancelDialog.dismiss();
				cancelDialog = null;
			} catch (Exception e) {
				// nothing
			}

			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
	}
	
	public void getData() throws IOException, MalformedURLException, NullPointerException {
		songsList = songManager.getPlayList();
		// looping through playlist
		for (int i = 0; i < songsList.size(); i++) {
			// creating new HashMap
			HashMap<String, String> song = songsList.get(i);

			// adding HashList to ArrayList
			songsListData.add(song);
		}
		
		adapter=new SimpleListAdapter(this, songsListData);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int currentPosition;
		switch(v.getId()) {
			case R.id.btnPrevious:
				if(isShuffle) {
					Random rand = new Random();
					currentSongIndex = rand.nextInt((songsList.size() - 1) - 0 + 1) + 0;
					playSong(currentSongIndex);
				} else {
					if(currentSongIndex > 0){
						playSong(currentSongIndex - 1);
						currentSongIndex = currentSongIndex - 1;
					}else{
						// play last song
						playSong(songsList.size() - 1);
						currentSongIndex = songsList.size() - 1;
					}
				}
				break;
			case R.id.btnBackward:
				// get current song position				
				currentPosition = mp.getCurrentPosition();
				// check if seekBackward time is greater than 0 sec
				if(currentPosition - seekBackwardTime >= 0){
					// forward song
					mp.seekTo(currentPosition - seekBackwardTime);
				}else{
					// backward to starting position
					mp.seekTo(0);
				}
				break;
			case R.id.btnPlay:
				if(mp.isPlaying()){
					if(mp!=null){
						mp.pause();
						// Changing button image to play button
						btnPlay.setImageResource(R.drawable.btn_play);
					}
				}else{
					// Resume song
					if(mp!=null){
						mp.start();
						// Changing button image to pause button
						btnPlay.setImageResource(R.drawable.btn_pause);
					}
				}
				
				break;
			case R.id.btnForward:
				// get current song position				
				currentPosition = mp.getCurrentPosition();
				// check if seekForward time is lesser than song duration
				if(currentPosition + seekForwardTime <= mp.getDuration()){
					// forward song
					mp.seekTo(currentPosition + seekForwardTime);
				}else{
					// forward to end position
					mp.seekTo(mp.getDuration());
				}
				break;
			case R.id.btnNext:
				if(isShuffle) {
					Random rand = new Random();
					currentSongIndex = rand.nextInt((songsList.size() - 1) - 0 + 1) + 0;
					playSong(currentSongIndex);
				} else {
					// check if next song is there or not
					if(currentSongIndex < (songsList.size() - 1)){
						playSong(currentSongIndex + 1);
						currentSongIndex = currentSongIndex + 1;
					}else{
						// play first song
						playSong(0);
						currentSongIndex = 0;
					}
				}
				break;
			case R.id.btnRepeat:
				if(isRepeat){
					isRepeat = false;
					Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
					btnRepeat.setImageResource(R.drawable.btn_repeat);
				}else{
					// make repeat to true
					isRepeat = true;
					Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
					// make shuffle to false
					isShuffle = false;
					btnRepeat.setImageResource(R.drawable.btn_repeat_focused);
					btnShuffle.setImageResource(R.drawable.btn_shuffle);
				}	
				break;
			case R.id.btnShuffle:
				if(isShuffle){
					isShuffle = false;
					Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
					btnShuffle.setImageResource(R.drawable.btn_shuffle);
					
					Random rand = new Random();
					currentSongIndex = rand.nextInt((songsList.size() - 1) - 0 + 1) + 0;
					playSong(currentSongIndex);
				}else{
					// make repeat to true
					isShuffle= true;
					Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
					// make shuffle to false
					isRepeat = false;
					btnShuffle.setImageResource(R.drawable.btn_shuffle_focused);
					btnRepeat.setImageResource(R.drawable.btn_repeat);
				}	
				break;				
		}
	}
	
	public void  playSong(int songIndex){
		// Play song
		try {
			if(DownloadActivity.down_mp != null) DownloadActivity.down_mp.stop();
			
        	mp.reset();
			mp.setDataSource(songsList.get(songIndex).get("songPath"));
			mp.prepare();
			mp.start();
			// Displaying Song title
			String songTitle = songsList.get(songIndex).get("songTitle");
        	songTitleLabel.setText(songTitle);
			
        	// Changing Button Image to pause image
			btnPlay.setImageResource(R.drawable.btn_pause);
			
			// set Progress bar values
			songProgressBar.setProgress(0);
			songProgressBar.setMax(100);
			
			// Updating progress bar
			updateProgressBar();			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);        
    }
	
	private Runnable mUpdateTimeTask = new Runnable() {
	   public void run() {
		   long totalDuration = mp.getDuration();
		   long currentDuration = mp.getCurrentPosition();
		  
		   // Displaying Total Duration time
		   songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
		   // Displaying time completed playing
		   songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));
		   
		   // Updating progress bar
		   int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
		   //Log.d("Progress", ""+progress);
		   songProgressBar.setProgress(progress);
		   
		   // Running this thread after 100 milliseconds
	       mHandler.postDelayed(this, 100);
	   }
	};

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		mHandler.removeCallbacks(mUpdateTimeTask);
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		mHandler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = mp.getDuration();
		int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);
		
		// forward or backward to certain seconds
		mp.seekTo(currentPosition);
		
		// update timer progress again
		updateProgressBar();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		// check for repeat is ON or OFF
		if(isRepeat){
			// repeat is on play same song again
			playSong(currentSongIndex);
		} else if(isShuffle){
			// shuffle is on - play a random song
			Random rand = new Random();
			currentSongIndex = rand.nextInt((songsList.size() - 1) - 0 + 1) + 0;
			playSong(currentSongIndex);
		} else{
			// no repeat or shuffle ON - play next song
			if(currentSongIndex < (songsList.size() - 1)){
				playSong(currentSongIndex + 1);
				currentSongIndex = currentSongIndex + 1;
			}else{
				// play first song
				playSong(0);
				currentSongIndex = 0;
			}
		}
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		mp.release();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
	}
}
