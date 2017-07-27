package com.anakarwin.apples.schedule;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.anakarwin.apples.R;
import com.anakarwin.apples.model.DateInfo;

/**
 * Created by yusuf on 7/23/2017.
 */

public class ScheduleViewHolder extends RecyclerView.ViewHolder {

	private TextView date;
	private TextView day;

	public ScheduleViewHolder(View itemView) {
		super(itemView);
		date = (TextView) itemView.findViewById(R.id.date);
		day = (TextView) itemView.findViewById(R.id.day);
	}

	public void bind(DateInfo dateInfo) {
		try {
			date.setText(String.valueOf(dateInfo.getDate().getDate()));
			switch (dateInfo.getStatusType()) {
				case READY:
					itemView.setBackgroundResource(R.color.ready);
					break;
				case HOLIDAY:
					itemView.setBackgroundResource(R.color.holiday);
					break;
				case DONE:
					itemView.setBackgroundResource(R.color.done);
					break;
				default:
					itemView.setBackgroundResource(R.color.ready);
					break;

			}
			if (dateInfo.getDate().getDay() == 0) {
				day.setText(itemView.getContext().getString(R.string.schedule_sunday));
			} else if (dateInfo.getDate().getDay() == 6) {
				day.setText(itemView.getContext().getString(R.string.schedule_saturday));
			} else {
				day.setText("-");
			}
		} catch (Exception e) {
			e.printStackTrace();
			date.setText("0");
			day.setText("-");
		}
	}
}
