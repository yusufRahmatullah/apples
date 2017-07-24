package com.anakarwin.apples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.anakarwin.apples.dashboard.DashboardFragment;
import com.anakarwin.apples.model.DAO;
import com.anakarwin.apples.schedule.ScheduleFragment;

public class MainActivity extends AppCompatActivity implements INavigateFragment {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		DAO.getInstance().initData(getApplicationContext());
		goToDashboard();
	}

	@Override
	public void onBackPressed() {
		getSupportFragmentManager().popBackStack();
	}

	@Override
	public void goToDashboard() {
		getSupportFragmentManager().beginTransaction()
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
				.replace(R.id.fragmentContainer, new DashboardFragment())
				.addToBackStack(ScheduleFragment.class.getSimpleName())
				.commit();
	}
}
