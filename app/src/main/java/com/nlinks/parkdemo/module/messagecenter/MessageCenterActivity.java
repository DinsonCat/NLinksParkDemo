package com.nlinks.parkdemo.module.messagecenter;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.module.messagecenter.fragment.InformationFragment;
import com.nlinks.parkdemo.module.messagecenter.fragment.MessageFragment;

/**
 * Created by Dell on 2017/04/17.
 */

public class MessageCenterActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    //初始化4个Fragment
    private Fragment mMsgFragment;
    private Fragment mInforFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);

        initView();//初始化所有的view
        setSelect(0);//默认显示界面
    }

    private void initView() {
        RadioGroup rbBottom = findViewById(R.id.rg_bottom);
        rbBottom.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rb_message:
                setSelect(0);
                break;
            case R.id.rb_information:
                setSelect(1);
                break;
        }
    }

    /**
     * 将图片设置为亮色的；切换显示内容的fragment
     */
    private void setSelect(int i) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();//创建一个事务
        hideFragment(transaction);//我们先把所有的Fragment隐藏了，然后下面再开始处理具体要显示的Fragment
        switch (i) {
            case 0:
                if (mMsgFragment == null) {
                    mMsgFragment = new MessageFragment();
                /*
                 * 将Fragment添加到活动中，public abstract FragmentTransaction add (int containerViewId, Fragment fragment)
                *containerViewId即为Optional identifier of the container this fragment is to be placed in. If 0, it will not be placed in a container.
                 * */
                    transaction.add(R.id.id_content, mMsgFragment);
                } else {
                    transaction.show(mMsgFragment);
                }
                break;
            case 1:
                if (mInforFragment == null) {
                    mInforFragment = new InformationFragment();
                    transaction.add(R.id.id_content, mInforFragment);
                } else {
                    transaction.show(mInforFragment);
                }
                break;
        }
        transaction.commit();//提交事务
    }

    /**
     * 隐藏所有的Fragment
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (mMsgFragment != null) {
            transaction.hide(mMsgFragment);
        }
        if (mInforFragment != null) {
            transaction.hide(mInforFragment);
        }
    }
}
