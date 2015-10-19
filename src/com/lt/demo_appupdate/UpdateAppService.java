package com.lt.demo_appupdate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateAppService extends Service {

	private String TAG = "lt";
	private static final int DOWNLOAD_ING = 1;
	private static final int DOWNLOAD_FINISH = 2;
	private String mAppURL;
	private NotificationManager mNotificationManager;
	private Builder mBuilder;
	private RemoteViews mRemoteViews;
	private Notification notification;
	private String mSavePath;
	private String mSaveName;
	private int mProgress;
	private IBinder mBinder = new LocalService();

	private boolean isCancelUpdate = false;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD_ING:
				Log.e(TAG, "DOWNLOAD_ING");
				mRemoteViews.setProgressBar(R.id.pb_app_update, 100, mProgress,
						false);
				Log.e(TAG, mProgress+"");
				mRemoteViews.setTextViewText(R.id.tv_app_update_progress,
						mProgress + "%");
				
				break;
			case DOWNLOAD_FINISH:
				Log.e(TAG, "DOWNLOAD_FINISH");
				isCancelUpdate = true;
				mRemoteViews.setProgressBar(R.id.pb_app_update, 100, 100,
						false);
				mRemoteViews.setTextViewText(R.id.tv_app_update_name, mSaveName
						+ ",apk ���ڰ�װ......");
				break;

			default:
				break;
			}
			//����֪ͨ
			mNotificationManager.notify(1, notification);
		}

	};

	@Override
	public void onCreate() {
		super.onCreate();
		Log.e(TAG, "onCreate");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "onDestroy");
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.e(TAG, "onBind");
		return mBinder;
	}

	class LocalService extends Binder {
		public UpdateAppService getService() {
			return UpdateAppService.this;
		}
	}

	public void updateApp(String URL, String name) {
		Log.e(TAG, "updapeApp");
		this.mAppURL = URL;
		this.mSaveName = name;
		createNotification();
		startDownload();
	}

	private void startDownload() {
		Log.e(TAG, "startDownload");
		Thread thread = new Thread(mDownApp);
		thread.start();
	}

	private Runnable mDownApp = new Runnable() {

		@Override
		public void run() {
			URL url;
			try {
				url = new URL(mAppURL);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				String sdpath = Environment.getExternalStorageDirectory() + "/";
				mSavePath = sdpath + "download";
				File file = new File(mSavePath);
				if (!file.exists()) {
					file.mkdir();
				}
				File apkFile = new File(file, mSaveName);
				Log.e(TAG, mSavePath);
				FileOutputStream fos = new FileOutputStream(apkFile);
				int count = 0;
				byte[] buffer = new byte[1024];
				int time=0;
				do {
					int readLength = is.read(buffer);
					count += readLength;
					mProgress = (int) (((float) count / length) * 100);
					Log.e(TAG, "do__while-----" + mProgress);
					//��ֹƵ�����³��ֿ���
					if(time==512){
						mHandler.sendEmptyMessage(DOWNLOAD_ING);
						time=0;
					}
					time++;			
					if (readLength <= 0) {
						mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
						Log.e(TAG, "end");
						break;
					}
					fos.write(buffer, 0, readLength);
				} while (!isCancelUpdate);
				fos.close();
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};

	@SuppressLint("NewApi")
	private void createNotification() {
		Log.e(TAG, "createNotification");
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mBuilder = new Builder(this);
		mRemoteViews = new RemoteViews(getPackageName(),
				R.layout.notification_update);
		mRemoteViews.setTextViewText(R.id.tv_app_update_name, mSaveName
				+ ".apk ��������......");
		mBuilder.setContent(mRemoteViews).setOngoing(true) 
				.setPriority(Notification.PRIORITY_DEFAULT)
				.setSmallIcon(R.drawable.ic_launcher)
				.setWhen(System.currentTimeMillis());
		notification = mBuilder.build();
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		mNotificationManager.notify(1, notification);
	}

}
