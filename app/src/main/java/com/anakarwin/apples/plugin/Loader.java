package com.anakarwin.apples.plugin;

import android.app.ProgressDialog;

/**
 * Created by E460 on 25/07/2017.
 */

public class Loader {

	private static final String LOADING_MESSAGE = "Please wait";
	private static final String LOADING_TITLE = "Loading";

	private ILoader view;
	private ProgressDialog pd;

	public Loader(ILoader ILoader) {
		this.view = ILoader;
		pd = new ProgressDialog(ILoader.getContext());
		pd.setMessage(LOADING_MESSAGE);
		pd.setTitle(LOADING_TITLE);
		pd.setCancelable(true);
	}

	public void setMessage(String message) {
		pd.setMessage(message);
	}

	public void setTitle(String title) {
		pd.setTitle(title);
	}

	public void setCancelable(boolean cancelable) {
		pd.setCancelable(cancelable);
	}

	public void startLoading() {
		if (pd != null) {
			pd.show();
		}
	}

	public void finishLoading() {
		if (pd != null) {
			pd.dismiss();
		}
	}
}
