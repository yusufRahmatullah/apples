package com.anakarwin.apples.payment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anakarwin.apples.R;
import com.anakarwin.apples.model.DAO;
import com.anakarwin.apples.model.Payment;

import java.util.List;

/**
 * Created by E460 on 26/07/2017.
 */

public class PaymentFragment extends Fragment {

	private List<Payment> payments;

	private RecyclerView listContainer;
	private RecyclerView.Adapter adapter;
	private RecyclerView.LayoutManager layoutManager;

	public PaymentFragment() {
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		initData();
		View view = inflater.inflate(R.layout.fragment_payment, container, false);
		listContainer = (RecyclerView) view.findViewById(R.id.listContainer);
		adapter = new PaymentAdapter(payments);
		layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
		listContainer.setAdapter(adapter);
		listContainer.setLayoutManager(layoutManager);
		listContainer.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
		return view;
	}

	private void initData() {
		payments = DAO.getInstance().getPayments();
	}
}
