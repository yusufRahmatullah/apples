package com.anakarwin.apples.topic;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anakarwin.apples.R;
import com.anakarwin.apples.model.DAO;
import com.anakarwin.apples.model.Topic;

import java.util.List;

/**
 * Created by E460 on 26/07/2017.
 */

public class TopicAdapter extends RecyclerView.Adapter<TopicViewHolder> {

	List<Topic> items;

	public TopicAdapter(List<Topic> items) {
		this.items = items;
	}

	@Override
	public TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic, parent, false);
		return new TopicViewHolder(view);
	}

	@Override
	public void onBindViewHolder(TopicViewHolder holder, int position) {
		holder.bind(items.get(position));
	}

	@Override
	public int getItemCount() {
		return items.size();
	}
}

class TopicViewHolder extends RecyclerView.ViewHolder {

	TextView level, date, content;

	TopicViewHolder(View itemView) {
		super(itemView);
		level = (TextView) itemView.findViewById(R.id.level);
		date = (TextView) itemView.findViewById(R.id.date);
		content = (TextView) itemView.findViewById(R.id.content);
	}

	public void bind(Topic topic) {
		try {
			level.setText(itemView.getContext().getString(R.string.class_format, topic.getLevel()));
			date.setText(DAO.dateFormatter.format(topic.getDate()));
			content.setText(topic.getContent());
		} catch (NullPointerException e) {
			level.setText("-");
			date.setText("-");
			content.setText("-");
		}
	}
}