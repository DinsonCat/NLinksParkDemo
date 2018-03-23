package com.nlinks.parkdemo.module.usercenter.parkrecord;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.ParkAPI;
import com.nlinks.parkdemo.api.PlateNumAPI;
import com.nlinks.parkdemo.entity.park.ParkRecord;
import com.nlinks.parkdemo.entity.plate.PlateInfo;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.usercenter.managecar.ManageCarActivity;
import com.nlinks.parkdemo.module.usercenter.parkrecord.adapter.ParkRecordAdapter;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.widget.recycleview.EmptyRecyclerView;
import com.nlinks.parkdemo.widget.recycleview.LinearRecyclerViewOnScroller;
import com.nlinks.parkdemo.widget.recycleview.LinearSpaceItemDecoration;
import com.nlinks.parkdemo.widget.recycleview.OnRecyclerItemClickListener;

import java.util.ArrayList;


/**
 * 停车记录（进行中，已结束统一）
 */
public class ParkRecordFragment extends Fragment {

    private ArrayList<ParkRecord> mDatas = new ArrayList<>();
    private Context mContext;

    //private CustomProgressDialog mCustomProgressDialog;//进度条
    private String mPlates;
    private int mParkState = 1;  //1进行中2已结束
    private static final String KEY_STATE = "state";
    private static final String KEY_PLATES = "plates";
    private SwipeRefreshLayout mSrlRefresher;//下拉刷新布局
    private EmptyRecyclerView mRvSingleList;
    private ParkRecordAdapter mAdapter;//适配器
    private LinearRecyclerViewOnScroller mScrollListener;//加载更多
    private OnRecyclerItemClickListener mClickListener;//点击监听
    private Integer mPage = 1;

    private TextView mEmptyAddPlate;//btn_manage_plate;
    private ImageView mEmptyImageView;
    private TextView mEmptyTextView;


    public static ParkRecordFragment newInstance(int parkState, String plates) {
        Bundle args = new Bundle();
        args.putInt(KEY_STATE, parkState);
        args.putString(KEY_PLATES, plates);
        ParkRecordFragment fragment = new ParkRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mPlates = bundle.getString(KEY_PLATES);
            mParkState = bundle.getInt(KEY_STATE);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    /*private void init() {
        mCustomProgressDialog = new CustomProgressDialog(mContext);
    }*/


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
        savedInstanceState) {

        View mRootView = inflater.inflate(R.layout.fragment_park_record, container, false);
        mSrlRefresher = mRootView.findViewById(R.id.srl_refresher);
        mRvSingleList = mRootView.findViewById(R.id.rv_single_list);
        View empty = mRootView.findViewById(R.id.v_empty);
        mRvSingleList.setEmptyView(empty);
        mEmptyAddPlate = mRootView.findViewById(R.id.btn_manage_plate);
        mEmptyImageView = mRootView.findViewById(R.id.iv_empty);
        mEmptyTextView = mRootView.findViewById(R.id.tv_empty);
        mEmptyAddPlate.setOnClickListener(v -> ManageCarActivity.start(getContext()));

        mScrollListener = new LinearRecyclerViewOnScroller(mSrlRefresher) {
            @Override
            public void loadMore() {
                loadNextData();
            }
        };
        mRvSingleList.addOnScrollListener(mScrollListener);

        mAdapter = new ParkRecordAdapter(mContext, mDatas);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRvSingleList.setAdapter(mAdapter);
        mRvSingleList.setLayoutManager(mLayoutManager);
        mRvSingleList.addItemDecoration(new LinearSpaceItemDecoration(0, 30));

        mClickListener = initItemClickListener(mRvSingleList);

        mRvSingleList.addOnItemTouchListener(mClickListener);

        //mSrlRefresher.setRefreshing(false);
        final SwipeRefreshLayout.OnRefreshListener listener = this::reloadData;
        mSrlRefresher.setOnRefreshListener(listener);

        //mSrlRefresher.setRefreshing(true);
        mSrlRefresher.post(listener::onRefresh);

        ViewParent parent = mRootView.getParent();
        if (parent != null) ((ViewGroup) parent).removeView(mRootView);
        return mRootView;

    }


    /**
     * 加载更多数据
     */
    private void loadNextData() {
        getDataFromServe(mPlates, false);
    }

    /**
     * 刷新数据
     */
    public void reloadData() {
        LogUtils.i("reloadData() called");
        // String userPlate = SPUtils.getUserPlate(mContext, "");
        if (StringUtils.isEmpty(mPlates)) {
            getPlate();
        } else {
            getDataFromServe(mPlates, true);
        }
    }


    private void getDataFromServe(String plate, final boolean reload) {

        if (plate.isEmpty()) {
            mEmptyAddPlate.setVisibility(View.VISIBLE);
            mEmptyTextView.setText("您还未添加车牌哦~");
            mEmptyImageView.setImageResource(R.mipmap.empty_park_recode_no_plate);

            mSrlRefresher.setRefreshing(false);
            return;
        } else {
            mEmptyAddPlate.setVisibility(View.GONE);
            mEmptyTextView.setText("您还没有停车记录哦~");
            mEmptyImageView.setImageResource(R.mipmap.empty_park_record);
        }

        if (reload) {
            mPage = 1;
            mSrlRefresher.setRefreshing(true);
        }
        HttpHelper.getRetrofit().create(ParkAPI.class).getParkRecord(plate, mParkState, mPage, 20)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<ArrayList<ParkRecord>>() {
            @NonNull
            @Override
            public void onHandleSuccess(ArrayList<ParkRecord> parkRecords) {
                if (reload) {
                    mAdapter.notifyDataSetChanged(parkRecords);
                } else {
                    mAdapter.notifyDataSetChangedNoClean(parkRecords);
                }
                mSrlRefresher.setRefreshing(false);
                mPage++;
            }

            @Override
            public void onHandleError(int code, String message) {
                super.onHandleError(code, message);
                mSrlRefresher.setRefreshing(false);
            }
        });
    }


    public OnRecyclerItemClickListener initItemClickListener(RecyclerView recyclerView) {
        return mClickListener != null ? mClickListener : new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onItemClick(View view, int position) {
                //传递数据
                if (mDatas.isEmpty() || position >= mDatas.size()) return;
                switch (mDatas.get(position).getRecordStatus()) {
                    case 1:
                    case 2:
                        //待缴费,预缴
                    case 4://欠费
                        ParkRecordDetailsForNoPayAty.start(getContext(), mDatas.get(position));
                        break;
                    case 3://已完成
                    case 5://等待离场
                        ParkRecordDetailsForSucAndLeaveAty.start(getContext(), mDatas.get(position));
                        break;
                }
                /*ParkRecordDeatilActivity.start((Activity) mContext, mDatas.get(position), ParkRecordActivity
                    .REQUEST_CODE);*/
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        };
    }

    /**
     * 请求车牌
     */
    private void getPlate() {
        String userId = SPUtils.getUserId(mContext, "");
        if (StringUtils.isEmpty(userId)) return;
        HttpHelper.getRetrofit().create(PlateNumAPI.class).getPlateList(userId)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<ArrayList<PlateInfo>>() {
            @NonNull
            @Override
            public void onHandleSuccess(ArrayList<PlateInfo> plateInfos) {
                StringBuilder plates = new StringBuilder();
                for (PlateInfo plateInfo : plateInfos) {
                    plates.append(plateInfo.getCarNo()).append(",");
                }

                //存sp
                SPUtils.putUserPlate(mContext, plates.toString());
                getDataFromServe(plates.toString(), true);
            }
        });
    }
}
