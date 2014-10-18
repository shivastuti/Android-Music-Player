package com.mp3.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StartupIntentReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent serviceIntent = new Intent(context, MusicBackgroundService.class);
        context.startService(serviceIntent);
        Log.i("Autostart", "started");
	}
}
