package com.anakarwin.apples.schedule;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anakarwin.apples.INavigateFragment;
import com.anakarwin.apples.OnListItemSelected;
import com.anakarwin.apples.R;
import com.anakarwin.apples.model.DAO;
import com.anakarwin.apples.model.DateInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by yusuf on 7/23/2017.
 */

public class ScheduleFragment extends Fragment {

	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM yyyy");

	private String monthText;
	private List<DateInfo> items;
	private RecyclerView listContainer;
	private RecyclerView.LayoutManager layoutManager;
	private ScheduleAdapter adapter;
	private INavigateFragment navigateFragment;
	private TextView monthTextView;
	private int currentMonth = 0;
	private int currentYear = 0;

	public ScheduleFragment() {
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof INavigateFragment) {
			navigateFragment = (INavigateFragment) context;
		}
		currentMonth = Calendar.getInstance().get(Calendar.MONTH);
		currentYear = Calendar.getInstance().get(Calendar.YEAR);
		initData();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_schedule, container, false);
		setHasOptionsMenu(true);
		monthTextView = ((TextView) view.findViewById(R.id.monthText));
		monthTextView.setText(monthText);
		adapter = new ScheduleAdapter(items);
		adapter.setOnListItemSelected(new OnListItemSelected() {
			@Override
			public void onItemSelected(int position) {
				navigateToDate(position);
			}
		});
		layoutManager = new GridLayoutManager(getContext(),
			getResources().getInteger(R.integer.schedule_span_size));
		listContainer = (RecyclerView) view.findViewById(R.id.listContainer);
		listContainer.setAdapter(adapter);
		listContainer.setLayoutManager(layoutManager);
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_schedule, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case R.id.actionPreviousMonth:
				currentMonth -= 1;
				if (currentMonth < Calendar.JANUARY) {
					currentMonth = Calendar.DECEMBER;
					currentYear -= 1;
				}
				break;
			case R.id.actionNextMonth:
				currentMonth += 1;
				if (currentMonth > Calendar.DECEMBER) {
					currentMonth = Calendar.JANUARY;
					currentYear += 1;
				}
				break;
		}
		initData();
		monthTextView.setText(monthText);
		adapter.updateItems(items);
		return false;
	}

	private void initData() {
		items = new ArrayList<>();

		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.YEAR, currentYear);
		cal.set(Calendar.MONTH, currentMonth);
		int maximum = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int minimum = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		monthText = simpleDateFormat.format(cal.getTime());
		for (int i = minimum; i <= maximum; i++) {
			cal.set(Calendar.DAY_OF_MONTH, i);
			int day = cal.get(Calendar.DAY_OF_WEEK);
			if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
				items.add(new DateInfo(cal.getTime()));
			}
		}

		// set date info
		List<DateInfo> dateInfos = DAO.getInstance().getDateInfos(currentYear, currentMonth, 1);
		Date now = new Date();
		for (DateInfo item : items) {
			boolean found = false;
			Date itemDate = item.getDate();
			for (DateInfo dateInfo : dateInfos) {
				Date recordedDate = dateInfo.getDate();
				if (itemDate.getYear() == recordedDate.getYear() &&
					itemDate.getMonth() == recordedDate.getMonth() &&
					itemDate.getDate() == recordedDate.getDate()) {
					item.setStatus(dateInfo.getStatus());
					found = true;
				}
			}
			if (!found) {
				if (itemDate.before(now)) {
					item.setStatusType(DateInfo.Type.DONE);
				} else {
					item.setStatusType(DateInfo.Type.READY);
				}
			}
		}
	}

	private void navigateToDate(int position) {
		Date date = items.get(position).getDate();
		if (navigateFragment != null && date != null) {
			navigateFragment.goToDateDetails(date.getYear(), date.getMonth(), date.getDate());
		}
	}
}
