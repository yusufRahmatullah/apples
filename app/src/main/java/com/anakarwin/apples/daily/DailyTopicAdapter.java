package com.anakarwin.apples.daily;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.anakarwin.apples.R;
import com.anakarwin.apples.plugin.AddableAdapter;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by E460 on 27/07/2017.
 */

public class DailyTopicAdapter extends RecyclerView.Adapter<DailyTopicVH> {

	private WeakReference<List<DailyTopicItem>> itemsRef;

	DailyTopicAdapter(List<DailyTopicItem> items) {
		itemsRef = new WeakReference<>(items);
	}

	@Override
	public DailyTopicVH onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily_topic, parent, false);
		return new DailyTopicVH(view);
	}

	@Override
	public void onBindViewHolder(DailyTopicVH holder, int position) {
		holder.bind(itemsRef.get().get(position));
	}

	@Override
	public int getItemCount() {
		if (itemsRef.get() != null) {
			return itemsRef.get().size();
		} else return 0;
	}

	public void updateDailyTopic() {
		List<DailyTopicItem> dailyTopicItems = itemsRef.get();
		if (dailyTopicItems != null) {

		}
	}
}

class DailyTopicVH extends RecyclerView.ViewHolder {

	private TextView level;
	private EditText content;

	DailyTopicVH(View itemView) {
		super(itemView);
		level = (TextView) itemView.findViewById(R.id.level);
		content = (EditText) itemView.findViewById(R.id.content);
	}

	public void bind(DailyTopicItem item) {
		if (item != null) {
			level.setText(itemView.getContext().getString(R.string.class_format, item.getLevel()));
			content.setText(item.getContent());
		}
	}
}
