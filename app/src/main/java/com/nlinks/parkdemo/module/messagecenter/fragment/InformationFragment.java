package com.nlinks.parkdemo.module.messagecenter.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.nlinks.parkdemo.api.MsgAPI;
import com.nlinks.parkdemo.entity.infomation.ActivityMsg;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.module.base.BaseListFragment;
import com.nlinks.parkdemo.module.base.WebViewNormalActivity;
import com.nlinks.parkdemo.module.usercenter.parkinformation.adapter.ActivityMsgAdapter;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.recycleview.LinearSpaceItemDecoration;

import io.reactivex.Observable;

/**
 * @author Dinson - 2017/9/6
 */
public class InformationFragment extends BaseListFragment<ActivityMsg> {
    private int mPage = 1;

    @Override
    public void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(new LinearSpaceItemDecoration(0, UIUtils.dip2px(10)));
    }

    @Override
    public RecyclerView.Adapter initAdapter() {
        return new ActivityMsgAdapter(getContext(), mDatas);
    }

    @Override
    public Observable initObservable(boolean reload) {
        if (reload) {
            mPage = 1;
        }
        return HttpHelper.getRetrofit().create(MsgAPI.class).getActivityMsg(mPage++, 20);
    }

    @Override
    public void onRvItemClick(ActivityMsg item) {
        super.onRvItemClick(item);
        Intent intent = new Intent(getContext(), WebViewNormalActivity.class);
        intent.putExtra(WebViewNormalActivity.EXTRA_TITLE, item.getTitle());
        intent.putExtra(WebViewNormalActivity.EXTRA_HTML, item.getContent());
        startActivity(intent);
    }
}
