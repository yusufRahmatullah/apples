package com.anakarwin.apples.schedule.dateDetails;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anakarwin.apples.INavigateFragment;
import com.anakarwin.apples.R;
import com.anakarwin.apples.model.DAO;
import com.anakarwin.apples.model.Present;

import java.util.Calendar;
import java.util.List;

/**
 * Created by E460 on 25/07/2017.
 */

public class DateDetailFragment extends Fragment {

	private static final String ARGS_YEAR = "year";
	private static final String ARGS_MONTH = "month";
	private static final String ARGS_DAY_OF_MONTH = "day_of_month";

	private INavigateFragment navigateFragment;
	private RecyclerView listContainer;
	private RecyclerView.Adapter adapter;
	private RecyclerView.LayoutManager layoutManager;
	private List<Present> presents;

	public static DateDetailFragment newInstance(int year, int month, int dayOfMonth) {

		Bundle args = new Bundle();
		args.putInt(ARGS_YEAR, year);
		args.putInt(ARGS_MONTH, month);
		args.putInt(ARGS_DAY_OF_MONTH, dayOfMonth);
		DateDetailFragment fragment = new DateDetailFragment();
		fragment.setArguments(args);
		return fragment;
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
		Bundle arguments = getArguments();
		if (arguments != null) {
			initData(arguments.getInt(ARGS_YEAR),
				arguments.getInt(ARGS_MONTH),
				arguments.getInt(ARGS_DAY_OF_MONTH));
		} else {
			initData(Calendar.getInstance().get(Calendar.YEAR),
				Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		}
		View view = inflater.inflate(R.layout.fragment_date_details, container, false);
		listContainer = (RecyclerView) view.findViewById(R.id.listContainer);
		adapter = DateDetailsAdapter.newInstance(presents);
		layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
		listContainer.setAdapter(adapter);
		listContainer.setLayoutManager(layoutManager);
		listContainer.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
		return view;
	}

	private void initData(int year, int month, int dayOfMonth) {
		presents = DAO.getInstance().getPresent(year, month, dayOfMonth);
	}
}
