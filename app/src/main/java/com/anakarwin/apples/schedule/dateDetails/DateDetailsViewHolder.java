package com.anakarwin.apples.schedule.dateDetails;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.anakarwin.apples.R;
import com.anakarwin.apples.model.Present;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

/**
 * Created by E460 on 25/07/2017.
 */

public class DateDetailsViewHolder extends RecyclerView.ViewHolder {

	TextView name;
	TextView level;

	public DateDetailsViewHolder(View itemView) {
		super(itemView);
		name = (TextView) itemView.findViewById(R.id.name);
		level = (TextView) itemView.findViewById(R.id.level);
	}

	public void bind(Present present) {
		try {
			name.setText(present.getStudent().getName());
			level.setText(String.valueOf(present.getStudent().getLevel()));
		} catch (Exception e) {
			name.setText("");
			level.setText("");
		}
	}
}
