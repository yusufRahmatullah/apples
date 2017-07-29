package com.anakarwin.apples.schedule.dateDetails;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.anakarwin.apples.INavigateFragment;
import com.anakarwin.apples.R;
import com.anakarwin.apples.model.DAO;
import com.anakarwin.apples.model.Present;
import com.anakarwin.apples.model.Student;

import java.util.Calendar;
import java.util.Date;
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
	private int year, month, date;

	private Dialog addDialog, delDialog;
	private TextInputLayout nameLayout, delNameLayout;
	private TextInputEditText nameET, delNameET;
	private Button doneBtn, delDoneBtn;

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
			year = arguments.getInt(ARGS_YEAR);
			month = arguments.getInt(ARGS_MONTH);
			date = arguments.getInt(ARGS_DAY_OF_MONTH);
			initData(year, month, date);
		} else {
			year = Calendar.getInstance().get(Calendar.YEAR);
			month = Calendar.getInstance().get(Calendar.MONTH);
			date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
			initData(year, month, date);
		}
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.fragment_date_details, container, false);
		listContainer = (RecyclerView) view.findViewById(R.id.listContainer);
		layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
		listContainer.setLayoutManager(layoutManager);
		listContainer.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
		setupAdapter();
		setupDialog();
		setupDeleteDialog();
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_date_details, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case R.id.action_add_present:
				addDialog.show();
				break;
			case R.id.action_delete_present:
				delDialog.show();
				break;
		}
		return true;
	}

	@Override
	public void onPause() {
		super.onPause();
		if (addDialog != null) {
			addDialog.dismiss();
		}
		if (delDialog != null) {
			delDialog.dismiss();
		}
	}

	private void setupDeleteDialog() {
		delDialog = new Dialog(getContext());
		delDialog.setCancelable(true);
		delDialog.setContentView(R.layout.dialog_delete_present);
		delDialog.setCanceledOnTouchOutside(true);
		delDialog.setTitle(R.string.present_delete_title);
		delNameLayout = (TextInputLayout) delDialog.findViewById(R.id.nameLayout);
		delNameET = (TextInputEditText) delDialog.findViewById(R.id.nameET);
		delDoneBtn = (Button) delDialog.findViewById(R.id.doneBtn);
		delDoneBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String name = delNameET.getText().toString();
				if (!name.isEmpty()) {
					Date currentDate = new Date(year, month, date);
					Present present = DAO.getInstance().getStudentPresent(name, currentDate);
					if (present != null) {
						DAO.getInstance().deletePresent(present);
						initData(year, month, date);
						setupAdapter();
						delDialog.dismiss();
					} else {
						delNameLayout.setError(getContext().getString(R.string.present_add_error_name_not_exist));
					}
				} else {
					delNameLayout.setError(getContext().getString(R.string.present_add_error_input));
				}
			}
		});
	}

	private void setupDialog() {
		addDialog = new Dialog(getContext());
		addDialog.setCancelable(true);
		addDialog.setContentView(R.layout.dialog_add_present);
		addDialog.setCanceledOnTouchOutside(true);
		addDialog.setTitle(R.string.present_add_title);
		nameLayout = (TextInputLayout) addDialog.findViewById(R.id.nameLayout);
		nameET = (TextInputEditText) addDialog.findViewById(R.id.nameET);
		doneBtn = (Button) addDialog.findViewById(R.id.doneBtn);
		doneBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String name = nameET.getText().toString();
				if (!name.isEmpty()) {
					Student student = DAO.getInstance().getStudent(name);
					if (student != null) {
						Date curentDate = new Date(year, month, date);
						Present present = new Present(curentDate, student);
						Present studentPresent = DAO.getInstance().getStudentPresent(student.getName(), curentDate);
						if (studentPresent == null) {
							DAO.getInstance().addPresent(present);
							initData(year, month, date);
							setupAdapter();
							addDialog.dismiss();
						} else {
							nameLayout.setError(getContext().getString(R.string.present_add_error_name_exist));
						}
					} else {
						nameLayout.setError(getContext().getString(R.string.present_add_error_name_not_exist));
					}
				} else {
					nameLayout.setError(getContext().getString(R.string.present_add_error_input));
				}
			}
		});
	}

	private void setupAdapter() {
		adapter = DateDetailsAdapter.newInstance(presents);
		listContainer.setAdapter(adapter);
	}

	private void initData(int year, int month, int dayOfMonth) {
		presents = DAO.getInstance().getPresent(year, month, dayOfMonth);
	}
}
