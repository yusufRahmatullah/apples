package com.anakarwin.apples.student;

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
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.anakarwin.apples.INavigateFragment;
import com.anakarwin.apples.OnListItemSelected;
import com.anakarwin.apples.R;
import com.anakarwin.apples.model.DAO;
import com.anakarwin.apples.model.Student;

import java.util.List;

/**
 * Created by E460 on 26/07/2017.
 */

public class StudentFragment extends Fragment {

	private RecyclerView listContainer;
	private StudentAdapter adapter;
	private RecyclerView.LayoutManager layoutManager;
	private INavigateFragment navigateFragment;
	private List<Student> students;

	private Dialog addDialog, deleteDialog;
	private TextInputLayout nameLayout, delNameLayout;
	private TextInputEditText nameET, levelET, delNameET;
	private Button doneBtn, delDoneBtn;

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
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.fragment_student, container, false);
		listContainer = (RecyclerView) view.findViewById(R.id.listContainer);
		layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
		listContainer.setLayoutManager(layoutManager);
		setupAdapter();
		listContainer.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
		setupDialog();
		setupDeleteDialog();
		return view;
	}

	private void setupAdapter() {
		if (adapter != null) {
			adapter.setOnListItemSelected(null);
			adapter.getParentItemList().clear();
		}
		adapter = StudentAdapter.newInstance(students);
		adapter.setOnListItemSelected(new OnListItemSelected() {
			@Override
			public void onItemSelected(int position) {
				navigateToStudentDetails(position);
			}
		});
		listContainer.setAdapter(adapter);
	}

	private void setupDialog() {
		addDialog = new Dialog(getContext());
		addDialog.setCancelable(true);
		addDialog.setContentView(R.layout.dialog_add_student);
		addDialog.setCanceledOnTouchOutside(true);
		addDialog.setTitle(R.string.student_add_title);
		nameLayout = (TextInputLayout) addDialog.findViewById(R.id.nameLayout);
		nameET = (TextInputEditText) addDialog.findViewById(R.id.nameET);
		levelET = (TextInputEditText) addDialog.findViewById(R.id.levelET);
		levelET.setInputType(InputType.TYPE_CLASS_NUMBER);
		doneBtn = (Button) addDialog.findViewById(R.id.doneBtn);
		doneBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String name = nameET.getText().toString();
				String level = levelET.getText().toString();
				if (!name.isEmpty() && !level.isEmpty()) {
					Student student = DAO.getInstance().getStudent(name);
					if (student == null) {
						DAO.getInstance().addStudent(new Student(name, Integer.parseInt(level)));
						initData();
						setupAdapter();
						addDialog.dismiss();
					} else {
						nameLayout.setError(getContext().getString(R.string.student_add_error_name_exist));
					}
				} else {
					nameLayout.setError(getContext().getString(R.string.student_add_error_input));
				}
			}
		});
	}

	private void setupDeleteDialog() {
		deleteDialog = new Dialog(getContext());
		deleteDialog.setCancelable(true);
		deleteDialog.setContentView(R.layout.dialog_delete_student);
		deleteDialog.setCanceledOnTouchOutside(true);
		deleteDialog.setTitle(R.string.student_delete_title);
		delNameLayout = (TextInputLayout) deleteDialog.findViewById(R.id.nameLayout);
		delNameET = (TextInputEditText) deleteDialog.findViewById(R.id.nameET);
		delDoneBtn = (Button) deleteDialog.findViewById(R.id.doneBtn);
		delDoneBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String name = delNameET.getText().toString();
				if (!name.isEmpty()) {
					Student student = DAO.getInstance().getStudent(name);
					if (student != null) {
						DAO.getInstance().deleteStudent(student);
						initData();
						setupAdapter();
						deleteDialog.dismiss();
					} else {
						delNameLayout.setError(getContext().getString(R.string.student_delete_error_name_not_exist));
					}
				} else {
					delNameLayout.setError(getContext().getString(R.string.student_add_error_input));
				}
			}
		});

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_student, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case R.id.action_add_student:
				addDialog.show();
				break;
			case R.id.action_delete_student:
				deleteDialog.show();
		}
		return true;
	}

	@Override
	public void onPause() {
		super.onPause();
		if (addDialog != null) {
			addDialog.dismiss();
		}
		if (deleteDialog != null) {
			deleteDialog.dismiss();
		}
	}

	private void initData() {
		students = DAO.getInstance().getStudents();
	}

	private void navigateToStudentDetails(int position) {
		if (students != null && position >= 0 && navigateFragment != null) {
			navigateFragment.goToStudentDetails(students.get(position).getName());
		}
	}
}
