package com.nlinks.parkdemo.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.widget.wheelpicker.WheelPicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 时间选择器
 * 2017年5月6日 新增的日期限制还未做完。有待完善2017年5月6日 12:22:50
 * Created by du on 2017/2/12.
 */
public class SingleDatetimePicker implements View.OnClickListener, WheelPicker.OnItemSelectedListener {

    public static interface OnDatetimeSelectedListener {
        public void onDatetimeSelected(Date date, int year, int month, int day, int hour, int minute);
    }

    public static class Builder {
        static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
        final Context mContext;
        OnDatetimeSelectedListener mOnDatetimeSelectedListener = null;
        boolean mHasDate = true, mHasTime = false, mHideYear = false, mAlwaysStartNow = false;
        Date mStartDate = null, mStopDate = null;
        TextView mTextView;
        String mFormat = DEFAULT_FORMAT;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setOnDatetimeSelectedListener(OnDatetimeSelectedListener onDatetimeSelectedListener) {
            mOnDatetimeSelectedListener = onDatetimeSelectedListener;
            return this;
        }

        public Builder setHasDate(boolean hasDate) {
            mHasDate = hasDate;
            return this;
        }

        public Builder setHasTime(boolean hasTime) {
            mHasTime = hasTime;
            return this;
        }

        public Builder setHideYear(boolean hideYear) {
            mHideYear = hideYear;
            return this;
        }

        public Builder setAlwaysStartNow(boolean alwaysStartNow) {
            mAlwaysStartNow = alwaysStartNow;
            return this;
        }

        public Builder setStartDate(Date startDate) {
            mStartDate = startDate;
            return this;
        }

        /**
         * 10位int型时间，以秒为单位
         *
         * @param startDateSeconds
         * @return
         */
        public Builder setStartDate(int startDateSeconds) {
            return setStartDate((long) startDateSeconds * 1000);
        }

        /**
         * 13位long型时间，以毫秒为单位
         *
         * @param startDateMills
         * @return
         */
        public Builder setStartDate(long startDateMills) {
            return setStartDate(new Date(startDateMills));
        }

        public Builder setStopDate(Date stopDate) {
            mStopDate = stopDate;
            return this;
        }

        /**
         * 10位int型时间，以秒为单位
         *
         * @param stopDateSeconds
         * @return
         */
        public Builder setStopDate(int stopDateSeconds) {
            return setStopDate((long) stopDateSeconds * 1000);
        }

        /**
         * 13位long型时间，以毫秒为单位
         *
         * @param stopDateMills
         * @return
         */
        public Builder setStopDate(long stopDateMills) {
            return setStopDate(new Date(stopDateMills));
        }

        public Builder withTextView(TextView textView) {
            mTextView = textView;
            return this;
        }

        public SingleDatetimePicker build() {
            return new SingleDatetimePicker(this);
        }

        public void showWithTextView() {
            showWithTextView(DEFAULT_FORMAT);
        }

        public void showWithTextView(String format) {
            if (mTextView != null) {
                mFormat = format;
                SingleDatetimePicker singleDatetimePicker = new SingleDatetimePicker(this);
                singleDatetimePicker.showAtLocation(mTextView);
            }
        }
    }

    private static Calendar generateDate(Date date) {
        return generateDate(date, 0, 0);
    }

    private static Calendar generateDate(Date date, int field, int amount) {
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        if (date != null) instance.setTime(date);
        if (amount != 0) instance.add(field, amount);
        return instance;
    }

    private static final int DEFAULT_MAX_YEAR = 100;

    private final Builder mBuilder;
    private int mMinYear, mMaxYear, mMinMonth, mMaxMonth, mMinDay, mMaxDay, mMinHour, mMaxHour, mMinMinute, mMaxMinute;

    private WheelPicker mWpPopYear;
    private WheelPicker mWpPopMonth;
    private WheelPicker mWpPopDay;
    private WheelPicker mWpPopHour;
    private WheelPicker mWpPopMinute;

    private PopupWindow mWindow;
    private OnDatetimeSelectedListener mOnDatetimeSelectedListener;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private final Calendar mCalendar = generateDate(null);

    //可选日期范围
    private final List<Integer> mYearList = new ArrayList<>(
        DEFAULT_MAX_YEAR + 1), mMonthList = new ArrayList<>(13), mDayList = new ArrayList<>(
        32), mHourList = new ArrayList<>(25), mMinuteList = new ArrayList<>(61);

    private SingleDatetimePicker(Builder builder) {
        mBuilder = builder;

        View rootView = LayoutInflater.from(mBuilder.mContext).inflate(R.layout.pop_pick_datetime, null);

        mWpPopYear = (WheelPicker) rootView.findViewById(R.id.wp_pop_1);
        mWpPopMonth = (WheelPicker) rootView.findViewById(R.id.wp_pop_2);
        mWpPopDay = (WheelPicker) rootView.findViewById(R.id.wp_pop_3);
        mWpPopHour = (WheelPicker) rootView.findViewById(R.id.wp_pop_4);
        mWpPopMinute = (WheelPicker) rootView.findViewById(R.id.wp_pop_5);

        rootView.findViewById(R.id.v_pick_city_pop_blank).setOnClickListener(this);
        rootView.findViewById(R.id.tv_pop_submit).setOnClickListener(this);
        rootView.findViewById(R.id.tv_pop_cancel).setOnClickListener(this);

        mWpPopYear.setOnItemSelectedListener(this);
        mWpPopMonth.setOnItemSelectedListener(this);
        mWpPopDay.setOnItemSelectedListener(this);
        mWpPopHour.setOnItemSelectedListener(this);
        mWpPopMinute.setOnItemSelectedListener(this);

        int width = WindowManager.LayoutParams.MATCH_PARENT;
        int height = WindowManager.LayoutParams.MATCH_PARENT;
        ColorDrawable drawable = new ColorDrawable(0x90000000);

        mWindow = new PopupWindow(rootView, width, height, true);
        mWindow.setOutsideTouchable(false);
        mWindow.setBackgroundDrawable(drawable);

        mOnDatetimeSelectedListener = mBuilder.mOnDatetimeSelectedListener;

        mWpPopYear.setVisibility(mBuilder != null && mBuilder.mHideYear ? View.GONE : View.VISIBLE);

        updateRange(5);

        hasDate(
            !(mBuilder.mHasDate || mBuilder.mHasTime) || mBuilder.mHasDate);//不能两个都为FALSE，当两个都为FALSE时，默认显示日期
        hasTime(mBuilder.mHasTime);
    }

    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
        if (picker == mWpPopYear) {
            mYear = mYearList.get(position);
            updateRange(4);
        } else if (picker == mWpPopMonth) {
            mMonth = mMonthList.get(position);
            updateRange(3);
        } else if (picker == mWpPopDay) {
            mDay = mDayList.get(position);
            updateRange(2);
        } else if (picker == mWpPopHour) {
            mHour = mHourList.get(position);
            updateRange(1);
        } else if (picker == mWpPopMinute) {
            mMinute = mMinuteList.get(position);
            mCalendar.set(Calendar.MINUTE, mMinute);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_pop_submit) {
            if (mOnDatetimeSelectedListener != null) {
                mOnDatetimeSelectedListener
                    .onDatetimeSelected(mCalendar.getTime(), mYear, mMonth, mDay, mHour, mMinute);
            }
            if (mBuilder.mTextView != null) {
                String date = mYear + "-" + mMonth + "-" + mDay + " " + mHour + ":" + mMinute;
                try {
                    date = new SimpleDateFormat(mBuilder.mFormat).format(mCalendar.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mBuilder.mTextView.setText(date);
            }
        }
        if (mWindow != null && mWindow.isShowing()) mWindow.dismiss();
    }

    private void initYearData() {
        mYear = mCalendar.get(Calendar.YEAR);
        if (mBuilder.mStopDate != null) {
            mMaxYear = generateDate(mBuilder.mStopDate, Calendar.SECOND, -1).get(Calendar.YEAR);
        } else {
            mMaxYear = mYear + DEFAULT_MAX_YEAR / 2;
        }
        if (mBuilder.mStartDate != null) {
            mMinYear = generateDate(mBuilder.mStartDate).get(Calendar.YEAR);
            mYear = mCalendar.get(Calendar.YEAR);
        } else {
            mMinYear = mMaxYear - DEFAULT_MAX_YEAR;
        }
        mYearList.clear();
        for (int index = mMinYear; index <= mMaxYear; index++) {
            mYearList.add(index);
        }
        if (mYearList.size() == 0) {
            mYearList.add(mYear);
        }
        mWpPopYear.setSuffix("年");
        mWpPopYear.setData(mYearList);
        mWpPopYear
            .setSelectedItemPosition(mYearList.indexOf(mYear));//mWpPopYear.setSelectedItemPosition(0);//
    }

    private void initMonthData() {
        mCalendar.set(Calendar.YEAR, mYear);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mMonthList.clear();
        mMinMonth = 1;
        mMaxMonth = 12;
        if (mMinYear == mYear) {
            mMinMonth = generateDate(mBuilder.mStartDate).get(Calendar.MONTH) + 1;
            mMonth = mMinMonth;
        }
        if (mMaxYear == mYear) {
            mMaxMonth = generateDate(mBuilder.mStopDate, Calendar.SECOND, -1).get(Calendar.MONTH) + 1;
        }
        for (int index = mMinMonth; index <= mMaxMonth; index++) {
            mMonthList.add(index);
        }
        if (mMonthList.size() == 0) {
            mMonthList.add(mMonth);
        }
        mWpPopMonth.setSuffix("月");
        mWpPopMonth.setData(mMonthList);
        mWpPopMonth
            .setSelectedItemPosition(mMinuteList.indexOf(mMonth));//mWpPopMonth.setSelectedItemPosition(0);//
    }

    private void initDayData() {
        try {//这个try里面处理了日期最大的bug
            Calendar first = (Calendar) mCalendar.clone();
            first.set(Calendar.DAY_OF_MONTH, 1);
            first.set(Calendar.MONTH, mMonth - 1);
            Calendar second = (Calendar) mCalendar.clone();
            second.set(Calendar.MONTH, mMonth - 1);
            if (first.get(Calendar.MONTH) != second.get(Calendar.MONTH)) {
                mCalendar.set(Calendar.DAY_OF_MONTH, first.getMaximum(Calendar.DAY_OF_MONTH));
                mCalendar.set(Calendar.MONTH, first.get(Calendar.MONTH));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mCalendar.set(Calendar.MONTH, mMonth - 1);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        mDayList.clear();
        final int maxDay = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        mMinDay = 1;
        mMaxDay = maxDay;
        if (mMinYear == mYear && mMinMonth == mMonth) {
            mMinDay = generateDate(mBuilder.mStartDate).get(Calendar.DAY_OF_MONTH);
        }
        if (mMaxYear == mYear && mMaxMonth == mMonth) {
            mMaxDay = generateDate(mBuilder.mStopDate, Calendar.SECOND, -1).get(Calendar.DAY_OF_MONTH);
        }
        for (int index = mMinDay; index <= mMaxDay; index++) {
            mDayList.add(index);
        }
        if (mDayList.size() == 0) {
            mDayList.add(mDay);
        }
        mWpPopDay.setSuffix("日");
        mWpPopDay.setData(mDayList);
        mWpPopDay.setSelectedItemPosition(mDayList.indexOf(mDay));//mWpPopDay.setSelectedItemPosition(0);//
    }

    private void initHourData() {
        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mHourList.clear();

        if (mMinYear == mYear && mMinMonth == mMonth && mMinDay == mDay) {
            mMinHour = generateDate(mBuilder.mStartDate).get(Calendar.HOUR_OF_DAY);
        } else mMinHour = 0;
        if (mMaxYear == mYear && mMaxMonth == mMonth && mMaxDay == mDay) {
            mMaxHour = generateDate(mBuilder.mStopDate, Calendar.SECOND, -1).get(Calendar.HOUR_OF_DAY) + 1;
        } else mMaxHour = 24;
        for (int index = mMinHour; index < mMaxHour; index++) {
            mHourList.add(index);
        }
        if (mHourList.size() == 0) {
            mHourList.add(mHour);
        }
        mWpPopHour.setSuffix("时");
        mWpPopHour.setData(mHourList);
        mWpPopHour
            .setSelectedItemPosition(mHourList.indexOf(mHour));//mWpPopHour.setSelectedItemPosition(0);//
    }

    private void initMinuteData() {
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mMinuteList.clear();

        if (mMinYear == mYear && mMinMonth == mMonth && mMinDay == mDay && mMinHour == mHour) {
            mMinMinute = generateDate(mBuilder.mStartDate).get(Calendar.MINUTE);
        } else mMinMinute = 0;
        if (mMaxYear == mYear && mMaxMonth == mMonth && mMaxDay == mDay && mMaxHour == mHour) {
            mMaxMinute = generateDate(mBuilder.mStopDate, Calendar.SECOND, -1).get(Calendar.MINUTE);
        } else mMaxMinute = 60;

        for (int index = mMinMinute; index < mMaxMinute; index++) {
            mMinuteList.add(index);
        }
        if (mMinuteList.size() == 0) {
            mMinuteList.add(mMinute);
        }
        mWpPopMinute.setSuffix("分");
        mWpPopMinute.setData(mMinuteList);
        mWpPopMinute.setSelectedItemPosition(
            mMinuteList.indexOf(mMinute));//mWpPopMinute.setSelectedItemPosition(0);//
    }

    public void updateStartDate(Date startDate) {
        if (mBuilder != null) {
            mBuilder.setStartDate(startDate);
            updateRange(5);
        }
    }

    public void updateStopDate(Date stopDate) {
        if (mBuilder != null) {
            mBuilder.setStopDate(stopDate);
            updateRange(5);
        }
    }

    /**
     * 刷新日期
     *
     * @param level 级别，1-5，传5即刷新年月日时分5项，传4即刷新月日时分4项，以此类推。默认刷新5级
     */
    public void updateRange(int level) {
        switch (level) {
            default:
            case 5:
                initYearData();
            case 4:
                initMonthData();
            case 3:
                initDayData();
            case 2:
                initHourData();
            case 1:
                initMinuteData();
        }
    }

    public void showAtLocation(View parent) {
        showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    public void showAtLocation(View parent, int gravity, int x, int y) {
        if (mWindow != null && !mWindow.isShowing()) {
            if (mBuilder != null && mBuilder.mAlwaysStartNow) {
                mBuilder.setStartDate(new Date());
                updateRange(5);
            }
            mWindow.showAtLocation(parent, gravity, x, y);
        }
    }

    public void hasDate(boolean has) {
        if (has) {
            mWpPopYear.setVisibility(mBuilder != null && !mBuilder.mHideYear ? View.VISIBLE : View.GONE);
            mWpPopMonth.setVisibility(View.VISIBLE);
            mWpPopDay.setVisibility(View.VISIBLE);
        } else {
            mWpPopYear.setVisibility(View.GONE);
            mWpPopMonth.setVisibility(View.GONE);
            mWpPopDay.setVisibility(View.GONE);
        }
    }

    public void hasTime(boolean has) {
        if (has) {
            mWpPopHour.setVisibility(View.VISIBLE);
            mWpPopMinute.setVisibility(View.VISIBLE);
        } else {
            mWpPopHour.setVisibility(View.GONE);
            mWpPopMinute.setVisibility(View.GONE);
        }
    }
}