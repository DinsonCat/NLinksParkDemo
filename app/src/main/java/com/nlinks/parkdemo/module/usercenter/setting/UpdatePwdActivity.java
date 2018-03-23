package com.nlinks.parkdemo.module.usercenter.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.nlinks.parkdemo.R;
import com.nlinks.parkdemo.api.UserApi;
import com.nlinks.parkdemo.entity._req.Updatepwd;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.HttpResult;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.module.base.BaseActivity;
import com.nlinks.parkdemo.utils.MD5;
import com.nlinks.parkdemo.utils.SPUtils;
import com.nlinks.parkdemo.utils.StringUtils;
import com.nlinks.parkdemo.utils.UIUtils;

import io.reactivex.Observable;

public class UpdatePwdActivity extends BaseActivity {

    private EditText et_old_pwd, et_new_pwd, et_new_pwd_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);

        initUI();
    }

    private void initUI() {
        et_old_pwd = (EditText) findViewById(R.id.et_old_pwd);
        et_new_pwd = (EditText) findViewById(R.id.et_new_pwd);
        et_new_pwd_confirm = (EditText) findViewById(R.id.et_new_pwd_confirm);
    }

    private String etFormat(EditText et) {
        return et.getText().toString().trim().replaceAll(" ", "");
    }

    public void onClickconfirm(View v) {

        Updatepwd entity = validateEt();
        if (entity == null) return;

        HttpHelper.getRetrofit().create(UserApi.class).updatePwd(entity)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<Void>(this) {
            @NonNull
            @Override
            public void onHandleSuccess(Void o) {
                UIUtils.showToast("修改成功");
                finish();
            }
        });
    }

    /**
     * 验证输入
     */
    private Updatepwd validateEt() {
        String old = etFormat(et_old_pwd);
        String newPwd = etFormat(et_new_pwd);
        String newPwd2 = etFormat(et_new_pwd_confirm);

        if (StringUtils.isEmpty(old)) {
            UIUtils.showToast("原密码不能为空");
            return null;
        }
        if (!newPwd.equals(newPwd2)) {
            UIUtils.showToast("两次密码不一致");
            return null;
        }
        if (newPwd2.length() < 6) {
            UIUtils.showToast("请输入至少6位密码");
            return null;
        }
        String phone = SPUtils.getLastPhone(this, "");
        if (StringUtils.isEmpty(phone)) {
            UIUtils.showToast("获取信息失败，退出后重新登录");
            return null;
        }
        return new Updatepwd(phone, MD5.encode(etFormat(et_old_pwd)), MD5.encode(etFormat(et_new_pwd)));
    }
}
