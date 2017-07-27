package com.anakarwin.apples.plugin;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anakarwin.apples.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by E460 on 27/07/2017.
 */

public abstract class AddableAdapter<T> extends RecyclerView.Adapter {

	private static final int TYPE_ADD = Integer.MIN_VALUE;

	protected List<T> items;

	public AddableAdapter(List<T> items) {
		if (items != null) {
			this.items = items;
		} else {
			this.items = new ArrayList<>();
		}
	}

	@Override
	public int getItemViewType(int position) {
		if (position == items.size()) {
			return TYPE_ADD;
		} else {
			return super.getItemViewType(position);
		}
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == TYPE_ADD) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addable, parent, false);
			return new AddViewHolder(view);
		} else {
			return onCreateOtherViewHolder(parent);
		}
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (holder.getItemViewType() == TYPE_ADD) {
			holder.itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					onAdd();
				}
			});
		} else {
			onBindOtherViewHolder(holder, position);
		}
	}

	@Override
	public int getItemCount() {
		return items.size() + 1;
	}

	protected T getItem(int position) {
		return items.get(position);
	}

	public abstract RecyclerView.ViewHolder onCreateOtherViewHolder(ViewGroup parent);

	public abstract void onBindOtherViewHolder(RecyclerView.ViewHolder holder, int position);

	public abstract void onAdd();

	public void updateItem(List<T> items) {
		this.items.clear();
		this.items.addAll(items);
		notifyDataSetChanged();
	}
}

class AddViewHolder extends RecyclerView.ViewHolder {

	public AddViewHolder(View itemView) {
		super(itemView);
	}
}
