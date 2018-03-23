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
package com.nlinks.parkdemo.utils.avalidations;

import android.text.TextUtils;
import android.widget.EditText;

import com.nlinks.parkdemo.utils.avalidations.utils.ValidationExecutor;

/**
 * 校验模型
 */
public class ValidationModel {
    private EditText editText;
    private ValidationExecutor validationExecutor;

    public ValidationModel(ValidationExecutor validationExecutor) {
        this.editText = validationExecutor.getEditText();
        this.validationExecutor = validationExecutor;
    }

    public EditText getEditText() {
        return editText;
    }

    public ValidationModel setEditText(EditText editText) {
        this.editText = editText;
        return this;
    }

    public ValidationExecutor getValidationExecutor() {
        return validationExecutor;
    }

    public ValidationModel setValidationExecutor(ValidationExecutor validationExecutor) {
        this.validationExecutor = validationExecutor;
        return this;
    }

    public boolean isTextEmpty() {
        if (editText == null || TextUtils.isEmpty(editText.getText())) {
            return true;
        }
        return false;
    }
}
