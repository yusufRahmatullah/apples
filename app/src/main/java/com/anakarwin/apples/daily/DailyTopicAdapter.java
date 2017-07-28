package com.anakarwin.apples.daily;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.anakarwin.apples.R;

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
		return new DailyTopicVH(view, new CustomTextChangeListener() {
			@Override
			public void onTextChanged(int position, String text) {
				List<DailyTopicItem> dailyTopicItems = itemsRef.get();
				if (dailyTopicItems != null && dailyTopicItems.size() > position && !text.trim().isEmpty()) {
					dailyTopicItems.get(position).setContent(text);
				}
			}
		});
	}

	@Override
	public void onBindViewHolder(DailyTopicVH holder, int position) {
		holder.setPosition(position);
		holder.bind(itemsRef.get().get(position));
	}

	@Override
	public int getItemCount() {
		if (itemsRef.get() != null) {
			return itemsRef.get().size();
		} else return 0;
	}
}

class DailyTopicVH extends RecyclerView.ViewHolder {

	private TextView level;
	private EditText content;
	private CustomTextChangeListener listener;

	DailyTopicVH(View itemView, CustomTextChangeListener listener) {
		super(itemView);
		level = (TextView) itemView.findViewById(R.id.level);
		content = (EditText) itemView.findViewById(R.id.content);
		content.addTextChangedListener(listener);
		this.listener = listener;
	}

	public void setPosition(int position) {
		listener.setPosition(position);
	}

	public void bind(final DailyTopicItem item) {
		if (item != null) {
			level.setText(itemView.getContext().getString(R.string.class_format, item.getLevel()));
			content.setText(item.getContent());
		}
	}
}

abstract class CustomTextChangeListener implements TextWatcher {

	private int position;

	public void setPosition(int position) {
		this.position = position;
	}

	@Override
	public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

	}

	@Override
	public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		onTextChanged(position, charSequence.toString());
	}

	@Override
	public void afterTextChanged(Editable editable) {

	}

	public abstract void onTextChanged(int position, String text);
}