package com.nlinks.parkdemo.module.appointment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nlinks.parkdemo.api.AppointmentApi;
import com.nlinks.parkdemo.entity.appointment.AppointmentRecord;
import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.module.base.BaseListFragment;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.recycleview.CommonAdapter;
import com.nlinks.parkdemo.widget.recycleview.LinearSpaceItemDecoration;

import io.reactivex.Observable;

/**
 * 预约记录
 * Created by Dell on 2017/05/02.
 */
public class AppointmentRecordFragment extends BaseListFragment<AppointmentRecord> {

    public static AppointmentRecordFragment newInstance(Integer flag) {
        AppointmentRecordFragment fragment = new AppointmentRecordFragment();
        fragment.mFlag = flag;
        return fragment;
    }

    private Integer mFlag;
    private int mPage = 1;
    private String mUserId;
    private AppointmentApi mApi;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mApi = HttpHelper.getRetrofit().create(AppointmentApi.class);
        mUserId = SPUtils.getUserId(getActivity(), null);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(new LinearSpaceItemDecoration(0, UIUtils.dip2px(10)));
    }

    @Override
    public CommonAdapter<AppointmentRecord> initAdapter() {
        return new AppointmentRecordAdapter(getActivity(), mDatas);
    }

    @Override
    public Observable initObservable(boolean reload) {
        if (reload) mPage = 1;
        if (mApi != null && mUserId != null) {
            return mApi.getAppointmentRecord(mUserId, mFlag, mPage++, AppConst.PAGE_SIZE_DEFAULT);
        }
        return null;
    }

    @Override
    public void onRvItemClick(AppointmentRecord item) {
        if (item != null) {
            String parkStatus = item.getParkStatus();
            if ("1".equals(parkStatus) || "2".equals(parkStatus) | "3".equals(parkStatus)) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppointmentRecordDetailActivity.SERIALIZABLE_APPOINTMENT_RECORD, item);
                ((BaseActivity) getActivity()).jumpTo(AppointmentRecordDetailActivity.class, bundle);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        loadData(true);
    }
}
