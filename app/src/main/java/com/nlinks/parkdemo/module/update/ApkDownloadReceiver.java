package com.nlinks.parkdemo.module.update;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.nlinks.parkdemo.utils.LogUtils;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * 升级文件下载完成广播
 * Created by Du JC on 2015/11/19.
 */
public class ApkDownloadReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, final Intent intent) {
		final DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
			DownloadManager.Query query = new DownloadManager.Query();
			//在广播中取出下载任务的id
			long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
			query.setFilterById(id);
			Cursor cursor = manager.query(query);
			int count = cursor.getCount();
			if (cursor.moveToFirst()) {
				//获取文件下载路径
				String uri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
				//如果文件名不为空，说明已经存在了，拿到文件名想干嘛都好
				LogUtils.d("-----------   download complete uri=    " + uri + "     " + count);
				if (uri != null) {
					Intent install = new Intent(Intent.ACTION_VIEW);
					install.addFlags(FLAG_ACTIVITY_NEW_TASK);//不会安装一半跳掉
					install.setDataAndType(Uri.parse(uri), "application/vnd.android.package-archive");
					try {
						context.startActivity(install);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
			cursor.close();
		} else if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(intent.getAction())) {
			try {
				Intent intent1 = new Intent();
				intent1.setFlags(FLAG_ACTIVITY_NEW_TASK);
				intent1.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
				context.startActivity(intent1);
			} catch (Exception e) {
			}
		}
	}
}
