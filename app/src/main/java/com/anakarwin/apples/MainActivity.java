package com.anakarwin.apples;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.anakarwin.apples.daily.DailyFragment;
import com.anakarwin.apples.dashboard.DashboardFragment;
import com.anakarwin.apples.payment.PaymentFragment;
import com.anakarwin.apples.schedule.ScheduleFragment;
import com.anakarwin.apples.schedule.dateDetails.DateDetailFragment;
import com.anakarwin.apples.student.StudentFragment;
import com.anakarwin.apples.student.studentDetails.StudentDetailsFragment;
import com.anakarwin.apples.topic.TopicFragment;

public class MainActivity extends AppCompatActivity implements INavigateFragment {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		goToDashboard();
	}

	@Override
	public void onBackPressed() {
		FragmentManager fm = getSupportFragmentManager();
		if (fm.getBackStackEntryCount() > 0) {
			fm.popBackStack();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void goToDashboard() {
		FragmentManager fm = getSupportFragmentManager();
		for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
			fm.popBackStack();
		}
		fm.beginTransaction()
			.replace(R.id.fragmentContainer, new DashboardFragment())
			.commit();
	}

	@Override
	public void goToSchedule() {
		getSupportFragmentManager().beginTransaction()
			.replace(R.id.fragmentContainer, new ScheduleFragment())
			.addToBackStack(ScheduleFragment.class.getSimpleName())
			.commit();
	}

	@Override
	public void goToStudent() {
		getSupportFragmentManager().beginTransaction()
			.replace(R.id.fragmentContainer, new StudentFragment())
			.addToBackStack(StudentFragment.class.getSimpleName())
			.commit();
	}

	@Override
	public void goToStudentDetails(String name) {
		getSupportFragmentManager().beginTransaction()
			.replace(R.id.fragmentContainer, StudentDetailsFragment.newInstance(name))
			.addToBackStack(StudentDetailsFragment.class.getSimpleName())
			.commit();
	}

	@Override
	public void goToDateDetails(int year, int month, int dayOfMonth) {
		getSupportFragmentManager().beginTransaction()
			.replace(R.id.fragmentContainer, DateDetailFragment.newInstance(year, month, dayOfMonth))
			.addToBackStack(DateDetailFragment.class.getSimpleName())
			.commit();
	}

	@Override
	public void goToPayments() {
		getSupportFragmentManager().beginTransaction()
			.replace(R.id.fragmentContainer, new PaymentFragment())
			.addToBackStack(PaymentFragment.class.getSimpleName())
			.commit();
	}

	@Override
	public void goToTopics() {
		getSupportFragmentManager().beginTransaction()
			.replace(R.id.fragmentContainer, new TopicFragment())
			.addToBackStack(TopicFragment.class.getSimpleName())
			.commit();
	}

	@Override
	public void goToDailyInput() {
		getSupportFragmentManager().beginTransaction()
			.replace(R.id.fragmentContainer, new DailyFragment())
			.addToBackStack(DailyFragment.class.getSimpleName())
			.commit();
	}

	@Override
	public void goToChangeData() {

	}
}
