package com.nlinks.parkdemo.module.messagecenter.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.MsgPushAPI;
import com.nlinks.parkdemo.entity.msgpush.PushMessage;
import com.nlinks.parkdemo.entity.msgpush.ServerMessage;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.base.BaseListFragment;
import com.nlinks.parkdemo.module.login.view.LoginActivity;
import com.nlinks.parkdemo.module.messagecenter.PushMessageDetailActivity;
import com.nlinks.parkdemo.module.messagecenter.adapter.MessageTypeFactory;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.recycleview.LinearSpaceItemDecoration;
import com.nlinks.parkdemo.widget.recycleview.MultiTypeAdapter;
import com.nlinks.parkdemo.widget.recycleview.TypeFactory;
import com.nlinks.parkdemo.widget.recycleview.Visitable;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author Dinson - 2017/9/6
 */
public class MessageFragment extends BaseListFragment<Visitable> {

    private int mPageSize = 20, mPage = 1;
    private View mNoLoginLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        //未登录，显示未登录布局
        FrameLayout rootView = (FrameLayout) view.findViewById(R.id.rootView);
        mNoLoginLayout = inflater.inflate(R.layout.mc_message_no_login, rootView, false);
        mNoLoginLayout.setVisibility(View.GONE);
        rootView.addView(mNoLoginLayout);
        mNoLoginLayout.findViewById(R.id.action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UIUtils.getContext(), LoginActivity.class));
            }
        });
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        String userId = SPUtils.getUserId(getContext());
        int visibility = mNoLoginLayout.getVisibility();
        mNoLoginLayout.setVisibility(StringUtils.isEmpty(userId) ? View.VISIBLE : View.GONE);

      if (StringUtils.isNotEmpty(userId) && visibility == View.VISIBLE) {
            loadData(true);
          LogUtils.e("onResume load data:");
        }

    }

    @Override
    public void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(new LinearSpaceItemDecoration(0, UIUtils.dip2px(10)));
    }

    @Override
    public RecyclerView.Adapter initAdapter() {
        mDatas.add(0, new ServerMessage());
        return new MessageAdapter(mDatas, new MessageTypeFactory());
    }

    @Override
    public Observable initObservable(boolean reload) {
        String userId = SPUtils.getUserId(getActivity(), null);
        if (!TextUtils.isEmpty(userId)) {
            if (reload) mPage = 1;
            return HttpHelper.getRetrofit().create(MsgPushAPI.class).getList(userId
                , mPage++, mPageSize);
        }
        return null;
    }

    @Override
    public void onRvItemClick(Visitable item) {
        if (item instanceof PushMessage) {
            PushMessageDetailActivity.start(getContext(), (PushMessage) item);
            ((PushMessage) item).setIsread(1);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadData(final boolean reload) {
        Observable observable = initObservable(reload);
        if (observable != null) {
            observable.compose(RxSchedulers.io_main()).subscribe(new BaseObserver<List<PushMessage>>() {
                @NonNull
                @Override
                public void onHandleSuccess(List<PushMessage> ts) {
                    if (reload) {
                        mDatas.clear();
                        mDatas.add(new ServerMessage());
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

    class MessageAdapter extends MultiTypeAdapter {
        public MessageAdapter(List<Visitable> modelList, TypeFactory typeFactory) {
            super(modelList, typeFactory);
        }

        @Override
        public int getRealItemCount() {
            int itemCount = getItemCount();
            if (itemCount >= 1) itemCount -= 1;
            return itemCount;
        }
    }
}
