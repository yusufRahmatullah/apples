package com.anakarwin.apples.daily;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.anakarwin.apples.INavigateFragment;
import com.anakarwin.apples.R;
import com.anakarwin.apples.model.DAO;
import com.anakarwin.apples.model.DateInfo;
import com.anakarwin.apples.model.Present;
import com.anakarwin.apples.model.Student;
import com.anakarwin.apples.model.Topic;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by E460 on 27/07/2017.
 */

public class DailyFragment extends Fragment {

	private RecyclerView presentList, topicList;
	private DailyPresentAdapter presentAdapter;
	private DailyTopicAdapter topicAdapter;
	private TextView dateTV;
	private DatePickerDialog datePickerDialog;
	private AlertDialog alertDialog;

	private List<Student> students;
	private List<DailyPresentItem> dailyPresentItems;
	private List<DailyTopicItem> dailyTopicItems;
	private Date currentDate;
	private INavigateFragment navigateFragment;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof INavigateFragment) {
			navigateFragment = (INavigateFragment) context;
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		initData();
		View view = inflater.inflate(R.layout.fragment_daily_input, container, false);
		presentList = (RecyclerView) view.findViewById(R.id.presentList);
		topicList = (RecyclerView) view.findViewById(R.id.topicList);
		dateTV = (TextView) view.findViewById(R.id.date);
		dateTV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				datePickerDialog.show();
			}
		});
		initDialogs();
		initView();
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_daily, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case R.id.action_refresh:
				refreshContent();
				break;
			case R.id.action_done:
				saveFinalData();
				break;
			case R.id.action_save:
				saveData();
				break;
		}
		return true;
	}


	private void initData() {
		currentDate = Calendar.getInstance().getTime();
		students = DAO.getInstance().getStudents();
		initPresentData();
		initTopicData();
	}

	private void initPresentData() {
		List<Present> presents = DAO.getInstance().getPresent(currentDate.getYear(), currentDate.getMonth(),
			currentDate.getDate());
		dailyPresentItems = new ArrayList<>();
		if (students != null && students.size() > 0) {
			if (presents != null && presents.size() > 0) {
				for (Student student : students) {
					boolean studentPresent = false;
					for (Present present : presents) {
						if (present.getStudent().getName().compareTo(student.getName()) == 0) {
							studentPresent = true;
						}
					}
					dailyPresentItems.add(new DailyPresentItem(student, studentPresent));
				}
			} else {
				for (Student student : students) {
					dailyPresentItems.add(new DailyPresentItem(student, false));
				}
			}
		}
	}

	private void initTopicData() {
		dailyTopicItems = new ArrayList<>();
		List<Topic> topics = DAO.getInstance().getTopics(currentDate.getYear(), currentDate.getMonth(),
			currentDate.getDate());
		// assume students sorted by level
		List<Integer> levels = new ArrayList<>();
		int currentLevel = 0;
		if (students != null && students.size() > 0) {
			for (Student student : students) {
				int level = student.getLevel();
				if (level != currentLevel) {
					currentLevel = level;
					levels.add(level);
				}
			}
		} else {
			for (int i = 1; i <= 12; i++) {
				levels.add(i);
			}
		}

		if (topics != null && topics.size() > 0) {
			for (Integer level : levels) {
				for (Topic topic : topics) {
					if (topic.getLevel() == level) {
						dailyTopicItems.add(new DailyTopicItem(topic.getLevel(), topic.getContent()));
					}
				}
			}
		} else {
			for (Integer level : levels) {
				dailyTopicItems.add(new DailyTopicItem(level, ""));
			}
		}
	}

	private void initDialogs() {
		alertDialog = new AlertDialog.Builder(getContext())
			.setMessage(getString(R.string.daily_alert_date_wrong))
			.setPositiveButton(R.string.daily_dialog_positive, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					alertDialog.dismiss();
					datePickerDialog.show();
				}
			})
			.setNegativeButton(R.string.daily_dialog_negative, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					alertDialog.dismiss();
				}
			})
			.create();

		datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker datePicker, int year, int month, int date) {
				Date chosenDate = new Date(year - 1900, month, date);
				if (chosenDate.getDay() == 6 || chosenDate.getDay() == 0) {
					currentDate = chosenDate;
					refreshContent();
				} else {
					datePickerDialog.dismiss();
					alertDialog.show();
				}
			}
		}, currentDate.getYear() + 1900, currentDate.getMonth(), currentDate.getDate());
	}

	private void initView() {
		presentAdapter = DailyPresentAdapter.newInstance(dailyPresentItems);
		RecyclerView.LayoutManager presentLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
			false);
		presentList.setAdapter(presentAdapter);
		presentList.setLayoutManager(presentLayout);
		presentList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

		topicAdapter = new DailyTopicAdapter(dailyTopicItems);
		RecyclerView.LayoutManager topicLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
			false);
		topicList.setAdapter(topicAdapter);
		topicList.setLayoutManager(topicLayout);
		topicList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

		dateTV.setText(DAO.dayDateFormatter.format(currentDate));
	}

	private void refreshContent() {
		initPresentData();
		presentAdapter = DailyPresentAdapter.newInstance(dailyPresentItems);
		presentList.setAdapter(presentAdapter);
		presentList.invalidate();
		initTopicData();
		topicAdapter = new DailyTopicAdapter(dailyTopicItems);
		topicList.setAdapter(topicAdapter);
		topicList.invalidate();
		dateTV.setText(DAO.dayDateFormatter.format(currentDate));
	}

	private void saveFinalData() {
		saveData();
		DAO.getInstance().saveDateInfo(new DateInfo(currentDate, DateInfo.Type.DONE));
		if (navigateFragment != null) {
			navigateFragment.goToDashboard();
		} else {
			getActivity().onBackPressed();
		}
	}

	private void saveData() {
		presentAdapter.updateDailyPresent();
		List<Present> presents = new ArrayList<>();
		for (DailyPresentItem item : dailyPresentItems) {
			if (item.isPresent()) {
				presents.add(new Present(currentDate, item.getStudent()));
			}
		}
		DAO.getInstance().savePresents(presents);

		List<Topic> topics = new ArrayList<>();
		for (DailyTopicItem item : dailyTopicItems) {
			if (!item.getContent().isEmpty()) {
				topics.add(new Topic(currentDate, item.getLevel(), item.getContent()));
			}
		}
		DAO.getInstance().saveTopics(topics);
	}
}
