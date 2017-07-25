package com.anakarwin.apples;

/**
 * Created by yusuf on 7/23/2017.
 */

public interface INavigateFragment {
	void goToDashboard();
	void goToSchedule();
	void goToStudent();
	void goToDateDetails(int year, int month, int dayOfMonth);
	void goToPayments();
	void goToTopics();
	void goToChangeData();
}
