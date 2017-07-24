package com.anakarwin.apples.dashboard;

import android.support.annotation.DrawableRes;

/**
 * Created by yusuf on 7/23/2017.
 */

public class DashboardItem {

	private int iconRes;
	private String title;

	public DashboardItem() {
	}

	public DashboardItem(@DrawableRes int iconRes, String title) {
		this.iconRes = iconRes;
		this.title = title;
	}

	public int getIconRes() {
		return iconRes;
	}

	public void setIconRes(int iconRes) {
		this.iconRes = iconRes;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
