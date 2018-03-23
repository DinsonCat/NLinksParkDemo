package com.nlinks.parkdemo.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.entity.park.ParkMain;
import com.nlinks.parkdemo.module.park.ParkDetailActivity;
import com.nlinks.parkdemo.module.park.navi.NaviCommon;
import com.nlinks.parkdemo.utils.MapUtil;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.TextColorBuilder;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.mzbanner.MZBannerView;
import com.nlinks.parkdemo.widget.mzbanner.holder.MZHolderCreator;
import com.nlinks.parkdemo.widget.mzbanner.holder.MZViewHolder;

import java.util.ArrayList;


/**
 * 一键停车
 */
public class DialogOnekeyPark extends Dialog implements View.OnClickListener {
    private ArrayList<ParkMain> mDatas;
    private MZBannerView mMZBanner;
    private NaviCommon mNavi;
    private BNRoutePlanNode mLocation;

    public DialogOnekeyPark(Context context, ArrayList<ParkMain> datas, BNRoutePlanNode location) {
        super(context, R.style.custom_dialog);

        this.mDatas = datas;
        this.mLocation = location;

        init(context);

    }


    private void init(final Context context) {
        setContentView(R.layout.dialog_onekey_park);
        mMZBanner = (MZBannerView) findViewById(R.id.banner);

        mMZBanner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {
                ParkMain park = mDatas.get(position);
                if (!StringUtils.isEmpty(park.getCode()))
                    ParkDetailActivity.start(context, park.getCode());
            }
        });
        mMZBanner.setPages(mDatas, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });


        mNavi = new NaviCommon((Activity) context);
        if (mNavi.initDirs()) {
            mNavi.initNavi();
        }

        findViewById(R.id.v_space1).setOnClickListener(this);
        findViewById(R.id.v_space2).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_space1:
            case R.id.v_space2:
                this.cancel();
                break;
        }
    }

    public class BannerViewHolder implements MZViewHolder<ParkMain> {
        private ImageView mImageView;
        private TextView mTvName, mTvAddress, mTvNum, tvParkDistance;
        private Button mBtnNavi;

        @Override
        public View createView(Context context) {
            // 返回页面布局文件
            View view = LayoutInflater.from(context).inflate(R.layout.pager_onekeypark, null);
            mImageView = (ImageView) view.findViewById(R.id.iv_park);
            mTvName = (TextView) view.findViewById(R.id.tv_park_name);
            mTvAddress = (TextView) view.findViewById(R.id.tv_park_address);
            mTvNum = (TextView) view.findViewById(R.id.tv_park_num);
            tvParkDistance = (TextView) view.findViewById(R.id.tv_park_distance);

            mBtnNavi = (Button) view.findViewById(R.id.btn_navi);
            return view;
        }

        @Override
        public void onBind(Context context, final int position, ParkMain bean) {
            String imageUrl = "";
            if (bean.getParkImageList() != null && !bean.getParkImageList().isEmpty()) {
                imageUrl = bean.getParkImageList().get(0);
            }
            Glide.with(context).load(imageUrl).placeholder(R.mipmap.bg_default_park).error(R.mipmap.bg_default_park).into(mImageView);

            mTvName.setText(bean.getName());
            mTvAddress.setText(bean.getAddress());

            if (bean.getUnuedStallNum() == -1) mTvNum.setVisibility(View.GONE);
            mTvNum.setText(TextColorBuilder.newBuilder().addTextPart("空车位：").addTextPart(context, R.color.mainactivity_yellow, bean.getUnuedStallNum()).buildSpanned());
            if (bean.getDistance()==0)tvParkDistance.setVisibility(View.GONE);
            tvParkDistance.setText(TextColorBuilder.newBuilder().addTextPart("距离：").addTextPart(context, R.color.mainactivity_yellow, MapUtil.getConvertDistance(bean.getDistance())).buildSpanned());

            mBtnNavi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doNavigation(mDatas.get(position));
                }
            });
        }

    }

    /**
     * 立即导航
     */
    public void doNavigation(ParkMain park) {
        View btn_navi = findViewById(R.id.btn_navi);
        btn_navi.setEnabled(false);

        double distance = MapUtil.getDistance(mLocation.getLatitude(), mLocation.getLongitude(), park.getLatitude(), park.getLongitude());
        if (distance < 100) {
            UIUtils.showToast("距离太近无法导航");
            btn_navi.setEnabled(true);
            return;
        }

        BNRoutePlanNode endNode = new BNRoutePlanNode(park.getLongitude(), park.getLatitude(), park.getName(), park.getAddress(), BNRoutePlanNode.CoordinateType.BD09LL);

        boolean navi = this.mNavi.routeplanToNavi(mLocation, endNode, btn_navi);

        if (!navi) {
            UIUtils.showToast("导航规划失败");
        }
    }


}