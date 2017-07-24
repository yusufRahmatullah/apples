package com.anakarwin.apples.dashboard;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anakarwin.apples.R;

/**
 * Created by yusuf on 7/23/2017.
 */

public class DashboardViewHolder extends RecyclerView.ViewHolder {

	private ImageView icon;
	private TextView title;

	public DashboardViewHolder(View itemView) {
		super(itemView);
		icon = (ImageView) itemView.findViewById(R.id.icon);
		title = (TextView) itemView.findViewById(R.id.title);
	}

	public void bind(DashboardItem item) {
		try {
			icon.setImageResource(item.getIconRes());
			title.setText(item.getTitle());
		} catch(Exception e) {
			icon.setImageResource(R.drawable.ic_default);
			title.setText("");
		}
	}
}
