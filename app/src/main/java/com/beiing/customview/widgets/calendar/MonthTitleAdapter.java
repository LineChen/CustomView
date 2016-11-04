package com.beiing.customview.widgets.calendar;

import android.view.View;
import android.view.ViewGroup;

public abstract class MonthTitleAdapter {

	public abstract View createTitleView(ViewGroup viewGroup);

	public abstract void bindTitleView(View child, int index);

}
