package com.nlinks.parkdemo.module.update;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.utils.LogUtils;

/**
 * 更新对话框
 * Created by Dell on 2017/06/05.
 */
public class UpdateDialog extends Dialog implements View.OnClickListener {

	private TextView mTvUpdateVersionName;
	private TextView mTvUpdateMessage;
	private String mDownloadUrl;

	public UpdateDialog(Context context) {
		super(context, R.style.custom_dialog);
		setContentView(R.layout.dialog_update);
		mTvUpdateVersionName = (TextView) findViewById(R.id.tv_update_version_name);
		mTvUpdateMessage = (TextView) findViewById(R.id.tv_update_message);
		findViewById(R.id.iv_update_close).setOnClickListener(this);
		findViewById(R.id.tv_update_immediately).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_update_close:
				dismiss();
				break;
			case R.id.tv_update_immediately:
				systemDownload(mDownloadUrl);
				dismiss();
				break;
		}
	}

	public void setDownloadUrl(String downloadUrl){
		mDownloadUrl = downloadUrl;
	}

	public void setUpdateMessage(String versionName, String updateMessage){
		mTvUpdateVersionName.setText("最新版本：" + versionName);
		mTvUpdateMessage.setText(updateMessage);//mTvUpdateMessage.setText(formatText(getContext(), updateMessage));
	}

	private void systemDownload(String url) {
		if (TextUtils.isEmpty(url)) return;
		DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
		Uri uri = Uri.parse(url);
		String lastPathSegment = uri.getLastPathSegment();
		LogUtils.d("lastPathSegment : " + lastPathSegment);
		DownloadManager.Request request = new DownloadManager.Request(uri);
		request.setMimeType("application/vnd.android.package-archive");
		request.setTitle(getContext().getResources().getString(R.string.app_name));
		request.setVisibleInDownloadsUi(true);
		request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS
				, lastPathSegment == null ? "app-release.apk"
						: !lastPathSegment.endsWith(".apk") ? lastPathSegment + ".apk" : lastPathSegment);
		downloadManager.enqueue(request);
	}

	@Override
	public void show() {
		Window window = getWindow();
		if (window != null) {
			WindowManager.LayoutParams attributes = window.getAttributes();
			if (attributes != null) {
				int intrinsicHeight = ContextCompat.getDrawable(getContext(), R.mipmap.update_close).getIntrinsicHeight();
				attributes.y = attributes.y - intrinsicHeight / 2;
				window.setAttributes(attributes);
			}
		}
		super.show();
	}

	/*private static Spanned formatText(Context context, String message){
		if (TextUtils.isEmpty(message)) return null;
		String[] split = message.split("\n");
		SpannableStringBuilder builder = new SpannableStringBuilder();
		int index = 0;

		for (String string : split){
			String trim;
			if (!TextUtils.isEmpty(string) && !TextUtils.isEmpty((trim = string.trim()))){
				if (index++ > 0) builder.append('\n');
				builder.append(' ');
				ImageSpan imageSpan = new ImageSpan(context, R.mipmap.update_circle);
				builder.addTextPart(trim);
			}
		}
		return builder.buildSpanned(context);
	}*/
}
