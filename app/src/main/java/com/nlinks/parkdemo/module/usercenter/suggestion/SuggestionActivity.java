package com.nlinks.parkdemo.module.usercenter.suggestion;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.FeedBackAPI;
import com.nlinks.parkdemo.entity._req.FeedBack;
import com.nlinks.parkdemo.entity.feedback.FbResult;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.module.usercenter.suggestion.adapter.SuggestionPicAdapter;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.CustomProgressDialog;
import com.nlinks.parkdemo.widget.NoScrollRecycleView;
import com.nlinks.parkdemo.widget.recycleview.GridSpaceItemDecoration;
import com.nlinks.parkdemo.widget.recycleview.OnRecyclerItemClickListener;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SuggestionActivity extends BaseActivity {

    private RadioGroup mRgType;
    private EditText mEtContent;
    private final int mMaxPic = 3;

    private ArrayList<String> mPicUrlList = new ArrayList<>();//图片方格的图片地址
    private String mPicUrlStr = "";//待上传的url字符串
    private NoScrollRecycleView mRvPic;
    private SuggestionPicAdapter mAdapter;//图片方格的适配器


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        initUI();
    }

    private void initUI() {
        mEtContent =   findViewById(R.id.et_content);
        mRgType =   findViewById(R.id.rg_type);
        mRgType.check(R.id.rb_price);

        mRvPic =   findViewById(R.id.rv_pic);
        mRvPic.setLayoutManager(new GridLayoutManager(this, 4));
        mRvPic.addItemDecoration(new GridSpaceItemDecoration(35, 0));

        mPicUrlList.add("android.resource://com.nlinks.parkdemo/mipmap/" + R.mipmap.yjfk_sczp);
        mRvPic.addOnItemTouchListener(new OnRecyclerItemClickListener(mRvPic) {
            @Override
            public void onItemClick(View view, int position) {
                if (position == mPicUrlList.size() - 1 && mPicUrlList.size() - 1 != mMaxPic) {
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
        mAdapter = new SuggestionPicAdapter(this, mPicUrlList);
        mRvPic.setAdapter(mAdapter);
    }

    /**
     * 点击提交按钮
     *
     * @param view
     */
    public void onClickSubmit(View view) {
        if (!validateSubmit()) return;
        updatePic();
    }

    private void updatePic() {
        if (mPicUrlIsEmpty()) {
            submitToServer();
            return;
        }
        ArrayList<RequestBody> bodys = new ArrayList<>();
        for (int i = 0; i < mPicUrlList.size() - 1; i++) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), new File(mPicUrlList.get(i)));
            bodys.add(requestBody);
        }
        FeedBackAPI api = HttpHelper.getRetrofit().create(FeedBackAPI.class);
        CustomProgressDialog pd = new CustomProgressDialog(this);
        Observable[] arrs = new Observable[bodys.size()];
        for (int i = 0; i < bodys.size(); i++) {
            arrs[i] = api.uploadPhoto(bodys.get(i));
        }

        mPicUrlStr = "";
        Observable.mergeArray(arrs).compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<FbResult>(this) {
                    @NonNull
                    @Override
                    public void onHandleSuccess(FbResult bean) {
                        LogUtils.e("上传成功..");
                        mPicUrlStr += bean.getFilePath() + ",";
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        LogUtils.e(mPicUrlStr + "\n正在提交服务器...");
                        submitToServer();
                    }
                });

    }

    /**
     * 提交到服务器
     */
    private void submitToServer() {
        FeedBack feedBackReq = getFeedBackReq();
        HttpHelper.getRetrofit().create(FeedBackAPI.class).submit(feedBackReq)
         .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<Void>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(Void o) {
                UIUtils.showToast("已提交成功，感谢您的支持");
                finish();
            }
        });
    }

    /**
     * 验证是否可以提交
     *
     * @return
     */
    private boolean validateSubmit() {
        String msg = mEtContent.getText().toString();
        if (StringUtils.isEmpty(msg)) {
            UIUtils.showToast("来都来了，不写点什么吗？");
            return false;
        }
        return true;
    }

    /**
     * 创建上传对象
     *
     * @return
     */
    private FeedBack getFeedBackReq() {
        String msg = mEtContent.getText().toString();
        String type = "";
        switch (mRgType.getCheckedRadioButtonId()) {
            case R.id.rb_price:
                type = "价格费用";
                break;
            case R.id.rb_other:
                type = "其他类目";
                break;
        }

        return new FeedBack("", type, msg, SPUtils.getUserId(this, ""), SPUtils.getLastPhone(this, ""), "", mPicUrlStr);
    }

    private static final int CODE_IMG = 209;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CODE_IMG) {
        }
    }

    private boolean mPicUrlIsEmpty() {
        return (mPicUrlList.size() <= 1 || mPicUrlList == null) ? true : false;
    }
}
