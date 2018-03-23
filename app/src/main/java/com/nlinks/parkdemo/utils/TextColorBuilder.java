package com.nlinks.parkdemo.utils;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;

/**
 * 多部分组成的text工具类，目前实现多种颜色
 * Created by Dell on 2017/04/18.
 */
public class TextColorBuilder {

	TextColorBuilder() {
	}

	public static TextColorBuilder newBuilder() {
		return new TextColorBuilder();
	}

	final StringBuilder mBuilder = new StringBuilder();

	public TextColorBuilder addTextPart(String text) {
		mBuilder.append(text);
		return this;
	}

	public TextColorBuilder addTextPart(Context context, int colorId, Object text) {
		return addTextPart(ContextCompat.getColor(context, colorId), text);
	}

	public TextColorBuilder addTextPart(int color, Object text) {
		mBuilder.append("<font color=\"");
		mBuilder.append(color);
		mBuilder.append("\">");
		mBuilder.append(text);
		mBuilder.append("</font>");
		return this;
	}

	public String buildHtml() {
		return mBuilder.toString();
	}

	public Spanned buildSpanned() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
				? Html.fromHtml(buildHtml(), Html.FROM_HTML_MODE_LEGACY)
				: Html.fromHtml(buildHtml());
	}

	@Override
	public String toString() {
		return mBuilder.toString();
	}
}
