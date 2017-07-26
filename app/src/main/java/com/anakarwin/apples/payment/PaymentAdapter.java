package com.anakarwin.apples.payment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anakarwin.apples.R;
import com.anakarwin.apples.model.DAO;
import com.anakarwin.apples.model.Payment;

import java.util.List;

/**
 * Created by E460 on 26/07/2017.
 */

public class PaymentAdapter extends RecyclerView.Adapter<PaymentViewHolder> {

	private List<Payment> items;

	public PaymentAdapter(List<Payment> items) {
		this.items = items;
	}

	@Override
	public PaymentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
			.inflate(R.layout.item_payment, parent, false);
		return new PaymentViewHolder(view);
	}

	@Override
	public void onBindViewHolder(PaymentViewHolder holder, int position) {
		holder.bind(items.get(position));
	}

	@Override
	public int getItemCount() {
		return items.size();
	}
}

class PaymentViewHolder extends RecyclerView.ViewHolder {

	TextView name, date;

	PaymentViewHolder(View itemView) {
		super(itemView);
		name = (TextView) itemView.findViewById(R.id.name);
		date = (TextView) itemView.findViewById(R.id.date);
	}

	public void bind(Payment payment) {
		try {
			name.setText(payment.getStudent().getName());
			date.setText(DAO.dateFormatter.format(payment.getDate()));
		} catch (NullPointerException e) {
			name.setText("-");
			date.setText("-");
		}
	}
}
