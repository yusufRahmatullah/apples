package com.anakarwin.apples.payment;

import android.app.Dialog;
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

import com.anakarwin.apples.R;
import com.anakarwin.apples.model.DAO;
import com.anakarwin.apples.model.Payment;
import com.anakarwin.apples.model.Student;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by E460 on 26/07/2017.
 */

public class PaymentFragment extends Fragment {

	private List<Payment> payments;

	private RecyclerView listContainer;
	private RecyclerView.Adapter adapter;
	private RecyclerView.LayoutManager layoutManager;

	private Dialog addDialog, delDialog;
	private TextInputLayout nameLayout, delNameLayout, delDateLayout;
	private TextInputEditText nameET, delNameET, delDateET;
	private Button doneBtn, delDoneBtn;

	public PaymentFragment() {
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		initData();
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.fragment_payment, container, false);
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
		inflater.inflate(R.menu.menu_payment, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case R.id.action_add_payment:
				addDialog.show();
				break;
			case R.id.action_delete_payment:
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

	private void setupAdapter() {
		adapter = new PaymentAdapter(payments);
		listContainer.setAdapter(adapter);
	}

	private void setupDialog() {
		addDialog = new Dialog(getContext());
		addDialog.setCancelable(true);
		addDialog.setContentView(R.layout.dialog_add_payment);
		addDialog.setCanceledOnTouchOutside(true);
		addDialog.setTitle(R.string.payment_add_title);
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
						DAO.getInstance().addPayment(new Payment(new Date(), student));
						initData();
						setupAdapter();
						addDialog.dismiss();
					} else {
						nameLayout.setError(getContext().getString(R.string.payment_add_error_name_not_exist));
					}
				} else {
					nameLayout.setError(getContext().getString(R.string.payment_add_error_input));
				}
			}
		});
	}

	private void setupDeleteDialog() {
		delDialog = new Dialog(getContext());
		delDialog.setCancelable(true);
		delDialog.setContentView(R.layout.dialog_delete_payment);
		delDialog.setCanceledOnTouchOutside(true);
		delDialog.setTitle(R.string.payment_delete_title);
		delNameLayout = (TextInputLayout) delDialog.findViewById(R.id.nameLayout);
		delDateLayout = (TextInputLayout) delDialog.findViewById(R.id.dateLayout);
		delNameET = (TextInputEditText) delDialog.findViewById(R.id.nameET);
		delDateET = (TextInputEditText) delDialog.findViewById(R.id.dateET);
		delDoneBtn = (Button) delDialog.findViewById(R.id.doneBtn);
		delDoneBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String name = delNameET.getText().toString();
				String dateString = delDateET.getText().toString();
				Date date = null;
				try {
					date = DAO.dateFormatter.parse(dateString);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (!name.isEmpty() && date != null) {
					Payment payment = DAO.getInstance().getStudentPayment(name, date);
					if (payment != null) {
						DAO.getInstance().deletePayment(payment);
						initData();
						setupAdapter();
						delDialog.dismiss();
					} else {
						delNameLayout.setError(getContext().getString(R.string.payment_delete_error_payment_not_exist));
					}
				} else {
					delNameLayout.setError(getContext().getString(R.string.payment_add_error_input));
					delDateLayout.setError(getContext().getString(R.string.payment_add_error_input));
				}
			}
		});
	}

	private void initData() {
		payments = DAO.getInstance().getPayments();
	}
}
