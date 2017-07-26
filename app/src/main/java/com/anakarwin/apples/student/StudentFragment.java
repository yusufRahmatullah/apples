package com.anakarwin.apples.student;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
		View view = inflater.inflate(R.layout.fragment_student, container, false);
		listContainer = (RecyclerView) view.findViewById(R.id.listContainer);
		adapter = StudentAdapter.newInstance(students);
		adapter.setOnListItemSelected(new OnListItemSelected() {
			@Override
			public void onItemSelected(int position) {
				navigateToStudentDetails(position);
			}
		});
		layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
		listContainer.setAdapter(adapter);
		listContainer.setLayoutManager(layoutManager);
		listContainer.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
		return view;
	}

	private void initData() {
		students = DAO.getInstance().getStudents();
	}

	private void navigateToStudentDetails(int position) {
		if (students != null && position >= 0 && navigateFragment != null) {
			navigateFragment.goToStudentDetails(students.get(0).getName());
		}
	}

}
