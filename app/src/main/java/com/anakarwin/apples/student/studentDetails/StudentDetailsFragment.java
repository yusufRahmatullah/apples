package com.anakarwin.apples.student.studentDetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anakarwin.apples.R;
import com.anakarwin.apples.model.DAO;
import com.anakarwin.apples.model.Present;
import com.anakarwin.apples.model.Student;

import java.util.Date;
import java.util.List;

/**
 * Created by E460 on 26/07/2017.
 */

public class StudentDetailsFragment extends Fragment {

	private static final String ARGS_NAME = "name";

	private Student student;
	private Date studentLastPayment;
	private List<Present> studentPresents;
	private TextView name, level, present, lastPayment, presentAfterPayment;

	public static StudentDetailsFragment newInstance(String name) {

		Bundle args = new Bundle();
		args.putString(ARGS_NAME, name);
		StudentDetailsFragment fragment = new StudentDetailsFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		Bundle arguments = getArguments();
		if (arguments != null) {
			String name = arguments.getString(ARGS_NAME, "");
			initData(name);
		}
		View view = inflater.inflate(R.layout.fragment_student_detail, container, false);
		name = (TextView) view.findViewById(R.id.name);
		level = (TextView) view.findViewById(R.id.level);
		present = (TextView) view.findViewById(R.id.present);
		presentAfterPayment = (TextView) view.findViewById(R.id.presentAfterPayment);
		lastPayment = (TextView) view.findViewById(R.id.lastPayment);
		bindView();
		return view;
	}

	private void initData(String name) {
		student = DAO.getInstance().getStudent(name);
		studentLastPayment = DAO.getInstance().getStudentPayments(name);
		studentPresents = DAO.getInstance().getStudentPresents(name);
	}

	private void bindView() {
		if (student != null) {
			name.setText(student.getName());
			level.setText(getContext().getString(R.string.class_format, student.getLevel()));
		} else {
			name.setText("-");
			level.setText(getContext().getString(R.string.class_format, 0));
		}

		if (studentLastPayment != null && studentPresents != null && studentPresents.size() > 0) {
			String dateString = DAO.dateFormatter.format(studentLastPayment);
			lastPayment.setText(getContext().getString(R.string.student_details_last_payment_format, dateString));
			int presentAfterLastPayment = 0;
			for (int i = 0; i < studentPresents.size(); i++) {
				Date stdDate = studentPresents.get(i).getDate();
				if (stdDate.getYear() == studentLastPayment.getYear() &&
					stdDate.getMonth() == studentLastPayment.getMonth() &&
					stdDate.getDate() == studentLastPayment.getDate()) {
					break;
				}
				presentAfterLastPayment += 1;
			}
			present.setText(getContext().getString(R.string.student_details_present_format, studentPresents.size()));
			presentAfterPayment.setText(
				getContext().getString(R.string.student_details_present_after_payment_format, presentAfterLastPayment));
		} else if (studentPresents != null && studentPresents.size() > 0) {
			present.setText(getContext().getString(R.string.student_details_present_format, studentPresents.size()));
		} else {
			lastPayment.setText(getContext().getString(R.string.student_details_last_payment_format, "-"));
			present.setText(getContext().getString(R.string.student_details_present_format, 0));
			presentAfterPayment.setText(
				getContext().getString(R.string.student_details_present_after_payment_format, 0));
		}
	}
}
