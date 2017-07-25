package com.anakarwin.apples.schedule;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anakarwin.apples.OnListItemSelected;
import com.anakarwin.apples.R;
import com.anakarwin.apples.model.DateInfo;

import java.util.List;

/**
 * Created by yusuf on 7/23/2017.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleViewHolder> {

	private List<DateInfo> items;
	private OnListItemSelected onListItemSelected;

	public ScheduleAdapter(List<DateInfo> items) {
		this.items = items;
	}

	@Override
	public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
			.inflate(R.layout.item_schedule, parent, false);
		return new ScheduleViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ScheduleViewHolder holder, final int position) {
		DateInfo dateInfo = items.get(position);
		holder.bind(dateInfo);
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onListItemSelected.onItemSelected(position);
			}
		});
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	public void setOnListItemSelected(OnListItemSelected onListItemSelected) {
		this.onListItemSelected = onListItemSelected;
	}

	public void updateItems(List<DateInfo> items) {
		this.items.clear();
		this.items.addAll(items);
		notifyDataSetChanged();
	}
}
