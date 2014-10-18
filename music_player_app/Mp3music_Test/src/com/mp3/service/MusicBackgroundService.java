package com.mp3.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.http.HttpWrapper;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class MusicBackgroundService extends Service {
	
	public MediaPlayer mediaPlayer;
	public static String playUrl;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Timer timer;
	   
	private TimerTask updateTask = new TimerTask() {
		@Override
		public void run() {
			try {
				mediaPlayer.setDataSource(playUrl);
				mediaPlayer.prepare();
				mediaPlayer.start();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	  
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		mediaPlayer = new MediaPlayer();
		
		timer = new Timer("MP3Timer");
	    timer.schedule(updateTask, 1000L, 60 * 1000L);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}		
}
