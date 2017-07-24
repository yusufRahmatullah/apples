package com.anakarwin.apples.dashboard;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anakarwin.apples.OnListItemSelected;
import com.anakarwin.apples.R;

import java.util.List;

/**
 * Created by yusuf on 7/23/2017.
 */

public class DashboardAdapter extends RecyclerView.Adapter<DashboardViewHolder> {

	private List<DashboardItem> items;
	private OnListItemSelected onListItemSelected;

	public DashboardAdapter(List<DashboardItem> items) {
		this.items = items;
	}

	@Override
	public DashboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_dashboard, parent, false);
		return new DashboardViewHolder(view);
	}

	@Override
	public void onBindViewHolder(DashboardViewHolder holder, final int position) {
		holder.bind(items.get(position));
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (onListItemSelected != null) {
					onListItemSelected.onItemSelected(position);
				}
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
}
