package com.scavanger.rearcam;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.scavanger.gpio.*;

public class RearService extends Service implements GPIOListener{

	private static final String TAG = "RearService";
	private static final int PORT = 3000;

	private GPIOWatcher watcher = null;

	public RearService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "Service started");
		return null;
	}

	@Override
	public void onCreate() {
		try {
			watcher = new GPIOWatcher(PORT);
		} catch (IOException e) {
			e.printStackTrace();
			Log.d(TAG, "Cant' init Watcher Thread");
		} catch (InterruptedException e) {
			e.printStackTrace();
			Log.d(TAG, "Watcher Thread interrupted.");
		}
		watcher.addListener(this);
		watcher.start();
	}
	
	@Override
	public void onDestroy() {
		Log.d(TAG, "Service destroyed");
		if (watcher != null)
			watcher.stop();
	}
	
	
	@Override
	public void onGPIOChanged(GPIOEvent e) {
		
		Log.d(TAG, "Status GPIO: " + (e.isOn() ? "On" : "Off"));
		
		if (e.isOn()) {
			Log.d(TAG, "Starting CamActivity");
			Intent camIntent = new Intent(getBaseContext(), com.camera.simplemjpeg.MjpegActivity.class);
			camIntent.putExtra("startedFromService", true);
			camIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			getApplication().startActivity(camIntent);
		} else {
			Log.d(TAG, "Closing CamActivity");
			sendBroadcast(new Intent("close"));
		}
		
	}
}
