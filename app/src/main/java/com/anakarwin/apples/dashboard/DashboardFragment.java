package com.anakarwin.apples.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anakarwin.apples.INavigateFragment;
import com.anakarwin.apples.OnListItemSelected;
import com.anakarwin.apples.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yusuf on 7/23/2017.
 */

public class DashboardFragment extends Fragment {

	private RecyclerView list;
	private RecyclerView.LayoutManager layoutManager;
	private DashboardAdapter adapter;
	private List<DashboardItem> items;
	private INavigateFragment navigateFragment;

	public DashboardFragment() {
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof INavigateFragment) {
			navigateFragment = (INavigateFragment) context;
		}
		initData();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
		list = (RecyclerView) view.findViewById(R.id.list);
		adapter = new DashboardAdapter(items);
		adapter.setOnListItemSelected(new OnListItemSelected() {
			@Override
			public void onItemSelected(int position) {
				navigateScreen(position);
			}
		});
		layoutManager = new GridLayoutManager(getContext(),
				getContext().getResources().getInteger(R.integer.dashboard_span_size));
		list.setLayoutManager(layoutManager);
		list.setAdapter(adapter);
		return view;
	}

	private void initData() {
		items = new ArrayList<>();
		items.add(new DashboardItem(R.drawable.ic_schedule,
				getString(R.string.dashboard_presents)));
		items.add(new DashboardItem(R.drawable.ic_students,
				getString(R.string.dashboard_students)));
		items.add(new DashboardItem(R.drawable.ic_payment, getString(R.string.dashboard_payments)));
		items.add(new DashboardItem(R.drawable.ic_topics, getString(R.string.dashboard_topics)));
		items.add(new DashboardItem(R.drawable.ic_change_datas, getString(R.string.dashboard_change_data)));
	}

	private void navigateScreen(int position) {
		if (navigateFragment != null) {
			switch (position) {
				case 0:
					navigateFragment.goToSchedule();
					break;
				case 1:
					navigateFragment.goToStudent();
					break;
				case 2:
					navigateFragment.goToPayments();
					break;
				case 3:
					navigateFragment.goToTopics();
					break;
				case 4:
					navigateFragment.goToChangeData();
					break;
				default:
					break;
			}
		}
	}


}
