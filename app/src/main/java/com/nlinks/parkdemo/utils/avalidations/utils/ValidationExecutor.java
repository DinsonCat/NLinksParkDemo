/**
 * Copyright 2014 ken.cai (http://www.shangpuyun.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nlinks.parkdemo.utils.avalidations.utils;


import android.content.Context;
import android.widget.EditText;

/**
 * 校验执行者
 */
public abstract class ValidationExecutor extends ValidationUtil {
    private EditText mEditText;
    public ValidationExecutor(EditText editText) {
        this.mEditText = editText;
    }

    /**
     * 这里做校验处理
     *
     * @return 校验成功返回true 否则返回false
     */
    public abstract boolean doValidate(Context context, String text);

    public EditText getEditText() {
        return mEditText;
    }

    public void setEditText(EditText editText) {
        mEditText = editText;
    }
}
