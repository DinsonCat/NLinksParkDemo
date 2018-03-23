package com.nlinks.parkdemo.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.nlinks.parkdemo.R;


/**
 * 提示框
 */
public class DialogCancelConfirm extends Dialog implements View.OnClickListener {

	OnOperationListener operationListener;

	public DialogCancelConfirm(Context context) {
		super(context, R.style.custom_dialog);
		setContentView(R.layout.dialog_cancel_confirm);
		findViewById(R.id.dialogLeftBtn).setOnClickListener(this);
		findViewById(R.id.dialogRightBtn).setOnClickListener(this);
	}

	public DialogCancelConfirm(Context context, View.OnClickListener listener) {
		super(context, R.style.custom_dialog);
		setContentView(R.layout.dialog_cancel_confirm);
		findViewById(R.id.dialogLeftBtn).setOnClickListener(this);
		findViewById(R.id.dialogRightBtn).setOnClickListener(listener);
	}

	public void setOperationListener(OnOperationListener operationListener) {
		this.operationListener = operationListener;
	}



	public void setMessage(CharSequence message) {
		((TextView) findViewById(R.id.dialogText)).setText(message);
	}

	public void setButtonsText(CharSequence left, CharSequence right) {
		findViewById(R.id.dialogRightBtn).setVisibility(View.VISIBLE);
		findViewById(R.id.left_right_divider).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.dialogLeftBtn)).setText(left);
		((TextView) findViewById(R.id.dialogRightBtn)).setText(right);
	}

	public void setButtonText(CharSequence text) {
		findViewById(R.id.btnLayout).setVisibility(View.GONE);
		findViewById(R.id.singleBtnLayout).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.singBtn)).setText(text);
		findViewById(R.id.singBtn).setOnClickListener(this);
	}



	public interface OnOperationListener {

		public void onLeftClick();

		public void onRightClick();
	}

	public void hideLeftButton() {
		findViewById(R.id.dialogLeftBtn).setVisibility(View.GONE);
		findViewById(R.id.left_right_divider).setVisibility(View.GONE);

	}

	public void hideRightButton() {
		findViewById(R.id.dialogRightBtn).setVisibility(View.GONE);
		findViewById(R.id.left_right_divider).setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialogLeftBtn:
			if (operationListener != null)
				operationListener.onLeftClick();
			else
				this.cancel();
			break;

		case R.id.dialogRightBtn:
			if (operationListener != null)
				operationListener.onRightClick();
			break;
		case R.id.singBtn:
			this.cancel();
			break;

		}
	}
}