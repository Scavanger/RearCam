package com.scavanger.rearcam;

import com.scavanger.rearcam.R;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class ServiceStopActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent serviceIntent = new Intent(this, RearService.class);
		stopService(serviceIntent);
		
		NotificationManager mNotificationManager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(1);
		
		Toast.makeText(this,R.string.rearServiceStoped, Toast.LENGTH_SHORT).show();
		
		finish();
	}
}
