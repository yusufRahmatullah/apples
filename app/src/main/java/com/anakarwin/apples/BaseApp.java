package com.anakarwin.apples;

import android.app.Application;

import com.anakarwin.apples.model.DAO;

/**
 * Created by E460 on 25/07/2017.
 */

public class BaseApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		DAO.getInstance().initRealm(getApplicationContext());
	}
}
