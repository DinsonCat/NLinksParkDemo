package com.nlinks.parkdemo.module.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.park.ParkMain;
import com.nlinks.parkdemo.module.park.ParkDetailActivity;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.widget.mzbanner.MZBannerView;
import com.nlinks.parkdemo.widget.mzbanner.holder.MZHolderCreator;
import com.nlinks.parkdemo.widget.mzbanner.holder.MZViewHolder;

import junit.framework.Test;

import java.util.ArrayList;

public class TestActivity extends BaseActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, TestActivity.class);
        context.startActivity(starter);



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);



        MZBannerView mMZBanner =  findViewById(R.id.banner);

        ArrayList<String> strings = new ArrayList<>();
        strings.add("http://ondlsj2sn.bkt.clouddn.com/FtnbcpaH5FeipZMV_PzvG_nP4svp.jpg");
        strings.add("http://ondlsj2sn.bkt.clouddn.com/FgoM0tniC1eDMUOLrI3-P89zk3yt.webp");
        strings.add("http://ondlsj2sn.bkt.clouddn.com/FgoM0tniC1eDMUOLrI3-P89zk3yt.webp");
        mMZBanner.setPages(strings, new MZHolderCreator<PicHolder>() {
            @Override
            public PicHolder createViewHolder() {
                return new PicHolder();
            }
        });

    }

    class PicHolder implements MZViewHolder<String>  {

        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(TestActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void onBind(Context context, int position, String data) {
            Glide.with(TestActivity.this).load(data).into(imageView);
        }
    }


}
