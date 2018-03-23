package com.nlinks.parkdemo.module.coupon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.nlinks.parkdemo.api.PayCouponAPI;
import com.nlinks.parkdemo.api.RedeemCodeApi;
import com.nlinks.parkdemo.entity._req.RedeemCode;
import com.nlinks.parkdemo.entity.park.ParkingCoupon;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.modle.CouponValidateExtra;
import com.nlinks.parkdemo.module.base.BaseListFragment;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.recycleview.LinearSpaceItemDecoration;
import com.nlinks.parkdemo.widget.recycleview.MultiTypeAdapter;
import com.nlinks.parkdemo.widget.recycleview.Visitable;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

/**
 * 停车券
 * Created by du on 2017/3/30.
 */
public class ParkingCouponFragment extends BaseListFragment<Visitable> implements OnExchangeListener {


    private int mFlag = 0;
    private CouponValidateExtra mExtra;

    private int mPageSize = 10, mPage = 1;

    private String mField = null, mDirection = null;
    private ObservableTransformer<Observable, ObservableSource> composeFunction = null;


    public static ParkingCouponFragment createInstance(int flag, CouponValidateExtra extra) {
        Bundle args = new Bundle();
        args.putInt("flag", flag);
        args.putParcelable("extra", extra);
        ParkingCouponFragment fragment = new ParkingCouponFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mFlag = getArguments().getInt("flag");
        mExtra =   getArguments().getParcelable("extra");

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(new LinearSpaceItemDecoration(0, UIUtils.dip2px(10)));
    }

    @Override
    public RecyclerView.Adapter initAdapter() {
        if (mFlag == 1) {
            mDatas.add(0, new Head());
        }
        return new MultiTypeAdapter(mDatas, new CounponUnusedTypeFactory(this, mExtra));
    }

    @Override
    public Observable initObservable(boolean reload) {
        String userId = SPUtils.getUserId(getActivity(), null);
        if (mFlag != 0 && !TextUtils.isEmpty(userId)) {
            if (reload) mPage = 1;
            return HttpHelper.getRetrofit().create(PayCouponAPI.class).getPayCouponList(mFlag
                , mPage++, mPageSize, mField, mDirection, userId);
        }
        return null;
    }


    @Override
    public void onRvItemClick(Visitable item) {
        if (item instanceof ParkingCoupon) {
            boolean validate = mExtra.validate((ParkingCoupon) item);
            if (((ParkingCoupon) item).getStatus() == 1 && validate) {
                Intent intent = new Intent();
                intent.putExtra(ParkingCouponActivity.RESULT_PARCELABLE_COUPON, ((ParkingCoupon) item));
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().onBackPressed();
            }
        }
    }

    @Override
    public void loadData(final boolean reload) {
        Observable observable = initObservable(reload);
        if (observable != null) {
            observable.compose(RxSchedulers.io_main()).subscribe(new BaseObserver<List<ParkingCoupon>>() {
                @NonNull
                @Override
                public void onHandleSuccess(List<ParkingCoupon> ts) {
                    if (reload) {
                        mDatas.clear();
                        if (mFlag == 1) {
                            mDatas.add(new Head());
                        }
                        addDefaultData();
                    }
                    mDatas.addAll(ts);
                    if (mAdapter != null) mAdapter.notifyDataSetChanged();
                    mSrlRefresher.setRefreshing(false);
                }

                @Override
                public void onHandleError(int code, String message) {
                    mSrlRefresher.setRefreshing(false);
                }
            });
        } else {
            mSrlRefresher.setRefreshing(false);
        }


    }

    /**
     * 点击了未使用优惠券的兑换
     */
    @Override
    public void onExchange(final View v, String code) {
        v.setEnabled(false);
        String userId = SPUtils.getUserId(mContext);
        if (StringUtils.isEmpty(userId)) return;
        RedeemCode entity = new RedeemCode(code, userId);
        LogUtils.i(entity.toString());

        HttpHelper.getRetrofit().create(RedeemCodeApi.class).getRedeemCode(entity)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<String>(getContext()) {
            @NonNull
            @Override
            public void onHandleSuccess(String s) {
                loadData(true);
            }

            @Override
            public void onComplete() {
                super.onComplete();
                v.setEnabled(true);
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }
}
