package com.anakarwin.apples.plugin;

import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by E460 on 25/07/2017.
 */

public class Refresher implements SwipeRefreshLayout.OnRefreshListener {

	private IRefresherView view;
	private SwipeRefreshLayout swipeRefreshLayout;

	public Refresher(IRefresherView view) {
		this.view = view;
//		swipeRefreshLayout = view.getHasLoaderView().findViewById(R.id.swipeRefreshLayout);
		swipeRefreshLayout.setOnRefreshListener(this);
	}

	@Override
	public void onRefresh() {
		if (swipeRefreshLayout != null) {
			swipeRefreshLayout.setRefreshing(true);
		}
		view.onRefresh();
	}

	public void finishRefresh() {
		if (swipeRefreshLayout != null) {
			swipeRefreshLayout.setRefreshing(false);
		}
	}
}
