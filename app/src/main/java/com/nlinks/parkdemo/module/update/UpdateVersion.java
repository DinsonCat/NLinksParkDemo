package com.nlinks.parkdemo.module.update;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.nlinks.parkdemo.api.CommonApi;
import com.nlinks.parkdemo.entity.common.LatestVersion;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.HttpResult;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.utils.UIUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * 更新
 * Created by Dell on 2017/05/25.
 */
public class UpdateVersion {

	private static UpdateVersion sVersion;
	public static void check(Context context, boolean showMessage){
		if (sVersion == null){
			synchronized (UpdateVersion.class){
				sVersion = new UpdateVersion(showMessage);
			}
		}
		sVersion.mShowMessage=showMessage;
		sVersion.checkLatestVersion(context);
	}

	private CommonApi mApi;
	private boolean mShowMessage;
	private boolean mInProcessed = false;//更新操作是否已经在进行中
	private boolean mDialogShowing = false;//更新对话框是否已经在显示中

	private UpdateVersion(boolean showMessage){
		mApi = HttpHelper.getRetrofit().create(CommonApi.class);
		mShowMessage = showMessage;
	}

	private void checkLatestVersion(final Context context) {
		if (mInProcessed || mDialogShowing) return;
		try {
			mInProcessed = true;
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			final int versionCode = pi.versionCode;
			if (versionCode > 0){
				Observable<HttpResult<LatestVersion>> observable = mApi.checkLatestVersion(versionCode);
				observable.throttleFirst(1, TimeUnit.SECONDS)
						.compose(RxSchedulers.io_main()).subscribe(new BaseObserver<LatestVersion>() {

					@NonNull
                    @Override
					public void onHandleSuccess(LatestVersion latestVersion) {
						if (latestVersion != null && latestVersion.getVersionCode() > versionCode){
							UpdateDialog dialog = new UpdateDialog(context);
							dialog.setDownloadUrl(latestVersion.getDownloadAddress());
							dialog.setUpdateMessage(latestVersion.getVersionName(), latestVersion.getUpdateInfo());
							dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
								@Override
								public void onDismiss(DialogInterface dialog) {
									mDialogShowing = false;
								}
							});
							mDialogShowing = dialog.isShowing();
							if (!mDialogShowing) {
								dialog.show();
								mDialogShowing = true;
							}
						}else if (mShowMessage){
							UIUtils.showToast("已是最新版本");
						}
					}

					@Override
					public void onNext(HttpResult<LatestVersion> value) {
						super.onNext(value);
						mInProcessed = false;
					}
				});
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			mInProcessed = false;
		}
	}

}
