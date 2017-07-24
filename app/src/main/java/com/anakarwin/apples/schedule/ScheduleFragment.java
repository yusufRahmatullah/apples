package com.anakarwin.apples.schedule;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anakarwin.apples.INavigateFragment;
import com.anakarwin.apples.OnListItemSelected;
import com.anakarwin.apples.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by yusuf on 7/23/2017.
 */

public class ScheduleFragment extends Fragment {

	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM yyyy");

	private String monthText;
	private List<ScheduleItem> items;
	private RecyclerView listContainer;
	private RecyclerView.LayoutManager layoutManager;
	private ScheduleAdapter adapter;
	private INavigateFragment navigateFragment;

	public ScheduleFragment() {
	}

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
		initData();
		View view = inflater.inflate(R.layout.fragment_schedule, container, false);
		((TextView) view.findViewById(R.id.monthText)).setText(monthText);
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

	private void initData() {
		items = new ArrayList<>();

		monthText = simpleDateFormat.format(Calendar.getInstance().getTime());
		int maximum = Calendar.getInstance().getMaximum(Calendar.DAY_OF_MONTH);
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		for (int i = 1; i <= maximum;) {
			int increment = 1;
			cal.set(Calendar.DAY_OF_MONTH, i);
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				items.add(new ScheduleItem(i));
			} else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				increment = 6;
				items.add(new ScheduleItem(i));
			}
			i += increment;
		}
	}

	private void navigateToDate(int date) {
		Log.e("debugdebug", "date: " + date);
	}
}
