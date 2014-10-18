package com.mp3.mp3music;

import java.io.BufferedInputStream;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.mp3.service.*;
import com.http.HttpWrapper;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.util.*;

public class SongView extends Activity implements OnClickListener, OnErrorListener,
OnPreparedListener, OnCompletionListener, SeekBar.OnSeekBarChangeListener {
	private Handler handler = new Handler();

	Button btnPlay, btnDownload, btnPreview;
	TextView textView1, textView2, textView3;
	SeekBar songProgressBar;
	TextView txtPercent, txtPro;

	public String playUrl;
	MediaPlayer mediaPlayer;
	private Utilities utils;
	private Handler mHandler = new Handler();
	
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	
	DatabaseHandler db;

	public Runnable notification;
	
	String title, file_name, time;
	
	ProgressBar progressBar;
	
	DownloadFileAsync download;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.song_details_view);
		
		db = new DatabaseHandler(this);
		
		mediaPlayer = new MediaPlayer();
		utils = new Utilities();
		
		btnPlay = (Button) findViewById(R.id.button2);
		btnPlay.setBackgroundResource(R.drawable.play2);
		btnPlay.setOnClickListener(this);
		
		btnDownload = (Button) findViewById(R.id.button1);
		btnDownload.setOnClickListener(this);
		btnPreview = (Button) findViewById(R.id.Button01);
		btnPreview.setOnClickListener(this);
		
		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);
		textView3 = (TextView) findViewById(R.id.textView3);
		songProgressBar = (SeekBar) findViewById(R.id.seekBar1);

		songProgressBar.setOnSeekBarChangeListener(this); // Important
		mediaPlayer.setOnCompletionListener(this); // Important
		
		Intent intent = getIntent();
	    // Receiving the Data
		title = intent.getStringExtra("item_text");
		time = intent.getStringExtra("item_info");
		file_name = intent.getStringExtra("item_link");
		
		MusicBackgroundService.playUrl = file_name;
		
		textView1.setText(intent.getStringExtra("item_text"));        
		textView2.setText(intent.getStringExtra("item_info"));
		textView3.setText(intent.getStringExtra("item_link"));		
		
		playUrl = intent.getStringExtra("item_link");
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.button1:
				db.addContact(new Contact(title, file_name, time));
				startDownload(playUrl);
				break;
			case R.id.Button01:
				
				if (!mediaPlayer.isPlaying()) {
					if(MusicActivity.mp != null) MusicActivity.mp.stop();
					if(DownloadActivity.down_mp != null) DownloadActivity.down_mp.stop();
					
					Log.d("Before playsong", "Media Player");
					playSong();
				} else {
					mediaPlayer.pause();
					btnPlay.setBackgroundResource(R.drawable.play2);
				}
				break;
			case R.id.button2:
				mediaPlayer.pause();
				btnPlay.setBackgroundResource(R.drawable.play2);
				break;
		}
	}
	
	public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);        
    }
	
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
		int totalDuration = mediaPlayer.getDuration();
		int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);
		
		// forward or backward to certain seconds
		mediaPlayer.seekTo(currentPosition);
		
		// update timer progress again
		updateProgressBar();
	}
	
	private Runnable mUpdateTimeTask = new Runnable() {
	   public void run() {
		   long totalDuration = mediaPlayer.getDuration();
		   long currentDuration = mediaPlayer.getCurrentPosition();
		  
		   // Updating progress bar
		   int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
		   //Log.d("Progress", ""+progress);
		   songProgressBar.setProgress(progress);
		   
		   // Running this thread after 100 milliseconds
	       mHandler.postDelayed(this, 100);
	   }
	};
	
	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void playSong() {

		Log.d("Inside playsong", "Media Player");

		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(playUrl);
			mediaPlayer.prepare();
			mediaPlayer.start();

			songProgressBar.setProgress(0);
			songProgressBar.setMax(100);
			
			updateProgressBar();
			// primarySeekBarProgressUpdater();
			btnPlay.setBackgroundResource(R.drawable.pause);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void startDownload(String url) {
		// custom dialog
		
		if(download == null) {
			download = new DownloadFileAsync();
			download.execute(url);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:
			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.custom);
			dialog.setTitle("Download...");
			
			TextView txtTitle = (TextView) dialog.findViewById(R.id.title);
			txtTitle.setText(title.toString());
			progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar1);
			
			txtPercent = (TextView) dialog.findViewById(R.id.txtPercent);
			txtPercent.setText("0%");
			txtPro = (TextView) dialog.findViewById(R.id.txtPro);
			txtPro.setText("0/100");
			
			Button cencel = (Button) dialog.findViewById(R.id.cancel);
			cencel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}				
			});
			
			Button background = (Button) dialog.findViewById(R.id.background);
			background.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					startService();
					dialog.dismiss();
				}
			});
			
			dialog.show();
			
			return dialog;
		default:
			return null;
		}
	}

	class DownloadFileAsync extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		@Override
		protected String doInBackground(String... aurl) {
			int count;

			try {

				URL url = new URL(aurl[0]);
				URLConnection conexion = url.openConnection();
				conexion.connect();

				int lenghtOfFile = conexion.getContentLength();
				Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

				InputStream input = new BufferedInputStream(url.openStream());
				String sdCard = Environment.getExternalStorageDirectory() + "/";
				File f = new File(sdCard, title + ".mp3");

				OutputStream output = new FileOutputStream(f);
				Log.d("SD_Card", sdCard);
				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
				Log.d("Error", e.toString());
			}
			return null;

		}

		protected void onProgressUpdate(String... progress) {
			Log.d("ANDRO_ASYNC", progress[0]);
			progressBar.setProgress(Integer.parseInt(progress[0]));
			
			txtPercent.setText(String.valueOf(Integer.parseInt(progress[0])) + "%");
			txtPro.setText(String.valueOf(Integer.parseInt(progress[0])) + "/100");
		}

		@Override
		protected void onPostExecute(String unused) {
			dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
		}
	}
	
	public void startService() {
		Intent service = new Intent(SongView.this, MusicBackgroundService.class); 
        this.startService(service);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		System.exit(0);
		finish();
	}
	
}
