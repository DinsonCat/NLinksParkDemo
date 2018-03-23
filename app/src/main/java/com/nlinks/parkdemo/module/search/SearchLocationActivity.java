package com.nlinks.parkdemo.module.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnItemTouchListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.module.park.LocationAdapter;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.MapUtil;
import com.nlinks.parkdemo.utils.TitleCompat;
import com.nlinks.parkdemo.utils.UIUtils;
import com.nlinks.parkdemo.widget.recycleview.HeaderAndFooterAdapter;
import com.nlinks.parkdemo.widget.recycleview.OnRecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页搜索界面
 * Created by Dell on 2017/04/24.
 */
public class SearchLocationActivity extends BaseActivity implements View.OnClickListener, OnGetPoiSearchResultListener, TextWatcher {

    public static final String BOOL_USE_VOICE = "BOOL_USE_VOICE";
    public static final String PARCELABLE_SEARCH_RESULT = "PARCELABLE_SEARCH_RESULT";



    private EditText mEtBarInput;
    private String mNowCity;


    private PoiSearch mPoiSearch = null;
    private boolean mUseVoice = false;//上级页面是否点击了讯飞语音
    private List<PoiInfo> mHistoryDatas = new ArrayList<>();


    private OnItemTouchListener mHistroyTouchListener;
    private OnItemTouchListener mSearchTouchListener;
    private List<PoiInfo> mSearchDatas = new ArrayList<>();

    private HeaderAndFooterAdapter mFooterAdapter;
    private LocationAdapter mSearchAdapter;

    private RecyclerView mRvSearchPoi;
    private RecyclerView mRvSearchHistory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        TitleCompat.setDefault(this);


        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);


        initExtraData();

        initView();

        initHistory();
    }

    private void initExtraData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mUseVoice = extras.getBoolean(BOOL_USE_VOICE);
        }
    }

    private void initView() {
        ImageView mIvBarVoiceInput = (ImageView) findViewById(R.id.iv_bar_voice_input);
        mIvBarVoiceInput.setOnClickListener(this);
        findViewById(R.id.iv_bar_back).setOnClickListener(this);
        findViewById(R.id.tv_bar_submit).setOnClickListener(this);

        mEtBarInput = (EditText) findViewById(R.id.et_bar_input);
        mEtBarInput.addTextChangedListener(this);

        mNowCity = MapUtil.getNowCity(this);
        if (TextUtils.isEmpty(mNowCity)) {
            mNowCity = getResources().getString(R.string.default_city);
        } else {
            mEtBarInput.setHint("搜索" + mNowCity + "周边目的地");
        }


        mRvSearchPoi = (RecyclerView) findViewById(R.id.rv_search_poi);
        mRvSearchPoi.setLayoutManager(new LinearLayoutManager(this));
        mSearchAdapter = new LocationAdapter(this, mSearchDatas);
        mRvSearchPoi.setAdapter(mSearchAdapter);
        mSearchTouchListener = new OnRecyclerItemClickListener(mRvSearchPoi) {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                PoiInfo poiInfo = mSearchDatas.get(position);

                //保存搜索历史记录
                MapUtil.setSearchHistory(SearchLocationActivity.this, poiInfo);

                intent.putExtra(PARCELABLE_SEARCH_RESULT, poiInfo);
                setResult(RESULT_OK, intent);
                onBackPressed();
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        };
        mRvSearchPoi.addOnItemTouchListener(mSearchTouchListener);




    }

    private void initHistory() {
        mRvSearchHistory = (RecyclerView) findViewById(R.id.rv_search_history);
        mHistoryDatas = MapUtil.getSearchHistory(this);
        mRvSearchHistory.setLayoutManager(new LinearLayoutManager(this));

        LocationAdapter  mHistoryAdapter = new LocationAdapter(this, mHistoryDatas);
        mFooterAdapter = new HeaderAndFooterAdapter(mHistoryAdapter);
        TextView mMFooter = getFooter();
        mFooterAdapter.addFootView(mMFooter);
        mRvSearchHistory.setAdapter(mFooterAdapter);
        mFooterAdapter.notifyDataSet();

        mRvSearchHistory.addOnItemTouchListener(new OnRecyclerItemClickListener(mRvSearchHistory) {
            @Override
            public void onItemClick(View view, int position) {
                int start= mFooterAdapter.getHeadersCount()-1;
                int end= mFooterAdapter.getRealItemCount()+start;


                if ( start<  position && position <= end) {
                    Intent intent = new Intent();
                    PoiInfo poiInfo = mHistoryDatas.get(position);
                    intent.putExtra(PARCELABLE_SEARCH_RESULT, poiInfo);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPoiSearch.destroy();
//        if (mRvSearchHistory != null && mItemTouchListener != null)
//            mRvSearchHistory.removeOnItemTouchListener(mItemTouchListener);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_bar_voice_input:

                break;
            case R.id.iv_bar_back:
                onBackPressed();
                break;
            case R.id.tv_bar_submit:
                finish();
                break;
            case R.id.tv_delete:
                if (mHistoryDatas!=null){
                    MapUtil.clearSearchHistory(this);
                    mRvSearchHistory.setVisibility(View.GONE);

                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        LogUtils.i("onGetPoiResult() called with: poiResult = [" + poiResult + "]");
        if (poiResult != null) {
            List<PoiInfo> allPoi = poiResult.getAllPoi();
            if (allPoi != null) {
                mSearchDatas.clear();
                mSearchDatas.addAll(allPoi);
                LogUtils.e(mSearchDatas.size() + "");
            }
        }
        setMode(MODE.POI);
        mRvSearchPoi.setVisibility(View.VISIBLE);
        mSearchAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String keyword = s.toString();
        LogUtils.d("------------- keyword : " + keyword);

        if (keyword.length() == 0) {
            setMode(MODE.HISTORY);
            return;
        }

        mPoiSearch.searchInCity(new PoiCitySearchOption()
                .city(mNowCity)
                .keyword(keyword)
                .pageCapacity(20)
                .pageNum(0));
    }


    enum MODE {
        HISTORY, POI
    }

    private void setMode(MODE mode) {
        mRvSearchPoi.setVisibility(mode == MODE.POI ? View.VISIBLE : View.GONE);
    }

    private TextView getFooter() {
        TextView tv = new TextView(this);
        tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setTextSize(18);
        tv.setTextColor(UIUtils.getColor(R.color.text_primary));
        tv.setPadding(0, 40, 0, 40);
        tv.setText("清除历史");
        tv.setId(R.id.tv_delete);
        tv.setBackgroundColor(UIUtils.getColor(R.color.white));
        tv.setOnClickListener(this);
        return tv;
    }
}
