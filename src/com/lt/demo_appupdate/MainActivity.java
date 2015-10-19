package com.lt.demo_appupdate;

import com.lt.demo_appupdate.UpdateAppService.LocalService;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

public class MainActivity extends Activity {

	private String TAG="lt";
	private Button mButton;
	private UpdateAppService mService;
	private TextImageView mImage;
	private TextView mText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mButton=(Button)findViewById(R.id.btn_test);
		
		mImage=(TextImageView)findViewById(R.id.iv_test);
		mImage.setText("�°�".charAt(0)+"");
		mText=(TextView)findViewById(R.id.tv_test);
		mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.e(TAG, "onClick");
				Intent intent=new Intent(MainActivity.this, UpdateAppService.class);
				startService(intent);
				bindService(intent, conn, Context.BIND_AUTO_CREATE);
			}
		});

	}
	
	
	
	ServiceConnection conn=new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService=null;
		}
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.e(TAG, "onServiceConnected");
			mService=((LocalService)service).getService();
			mService.updateApp("http://gdown.baidu.com/data/wisegame/fb3bba73b5437246/QQ_264.apk", "腾讯QQ");
		}
	};
}
