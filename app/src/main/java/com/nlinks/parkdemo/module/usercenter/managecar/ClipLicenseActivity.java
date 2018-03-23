package com.nlinks.parkdemo.module.usercenter.managecar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.utils.DateUtils;
import com.nlinks.parkdemo.utils.ImageFactory;
import com.nlinks.parkdemo.widget.clipimageview.ClipImageLayout;

import java.io.File;


/**
 * 用户头像截图
 */
public class ClipLicenseActivity extends AppCompatActivity implements OnClickListener {

    private static final String EXTRA_IMAGEPATH = "ImagePath";
    public static final String KEY_OUTPUT_PATH = "output_path";

    public static void start(Activity context, String imagePath,int requestCode) {
        Intent starter = new Intent(context, ClipLicenseActivity.class);
        starter.putExtra(EXTRA_IMAGEPATH, imagePath);
        context.startActivityForResult(starter, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clipimage);

        initUI();

    }

    private void initUI() {
        findViewById(R.id.tv_to_used).setOnClickListener(this);
        findViewById(R.id.ib_back).setOnClickListener(this);
        ClipImageLayout clipImageLayout = (ClipImageLayout) findViewById(R.id.clipImageLayout);
        String stringExtra = getIntent().getStringExtra(EXTRA_IMAGEPATH);
        clipImageLayout.setDrawable(stringExtra);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_to_used:
                 ClipImageLayout clipImageLayout = (ClipImageLayout) findViewById(R.id.clipImageLayout);
                Bitmap clip = clipImageLayout.clip();
                //获取当前时间用于头像名称
                String currentDateTime = DateUtils.getCurrentDateTime("yyyyMMddHHmmss");
                //转化为文件
                String cacheImgDir = AppConst.CACHE_IMG_DIR;
                File file = new File(cacheImgDir);
                if (!file.exists())
                    file.mkdir();
                new ImageFactory().compressAndGenImage(clip, cacheImgDir + currentDateTime + ".jpg", 100);
                Intent intent = new Intent();
                intent.putExtra(KEY_OUTPUT_PATH,cacheImgDir +currentDateTime+".jpg");
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.ib_back:
                finish();
                break;
        }
    }
}
