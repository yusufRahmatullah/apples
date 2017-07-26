package com.anakarwin.apples.schedule.dateDetails;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anakarwin.apples.R;
import com.anakarwin.apples.model.Present;
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by E460 on 25/07/2017.
 */

public class DateDetailsAdapter extends ExpandableRecyclerAdapter<DateDetailsParentVH, DateDetailsChildVH> {

	public DateDetailsAdapter(@NonNull List<? extends ParentListItem> parentItemList) {
		super(parentItemList);
	}

	public static DateDetailsAdapter newInstance(List<Present> presents) {
		// assume present is ordered by student level
		if (presents.size() > 0) {
			List<DateDetailsParent> parents = new ArrayList<>();
			int currentLevel = presents.get(0).getStudent().getLevel();
			List<Integer> parentPos = new ArrayList<>();
			parentPos.add(0); // first parent position
			for (int i = 0; i < presents.size(); i++) {
				Present present = presents.get(i);
				if (present.getStudent().getLevel() !=  currentLevel) {
					currentLevel = present.getStudent().getLevel();
					parentPos.add(i);
				}
			}
			for (int i = 0; i < parentPos.size(); i++) {
				int from  = parentPos.get(i);
				int to;
				int level = presents.get(from).getStudent().getLevel();
				if (i == parentPos.size() - 1) {
					to = presents.size();
				} else {
					to = parentPos.get(i + 1);
				}
				parents.add(new DateDetailsParent(presents.subList(from, to), level));
			}
			return new DateDetailsAdapter(parents);
		} else {
			return new DateDetailsAdapter(new ArrayList<DateDetailsParent>());
		}
	}

	@Override
	public DateDetailsParentVH onCreateParentViewHolder(ViewGroup parentViewGroup) {
		View view = LayoutInflater.from(parentViewGroup.getContext())
			.inflate(R.layout.item_date_details_parent, parentViewGroup, false);
		return new DateDetailsParentVH(view);
	}

	@Override
	public DateDetailsChildVH onCreateChildViewHolder(ViewGroup childViewGroup) {
		View view = LayoutInflater.from(childViewGroup.getContext())
			.inflate(R.layout.item_date_details_child, childViewGroup, false);
		return new DateDetailsChildVH(view);
	}

	@Override
	public void onBindParentViewHolder(DateDetailsParentVH parentViewHolder, int position,
	                                   ParentListItem parentListItem) {
		try {
			parentViewHolder.bind((DateDetailsParent) parentListItem);
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	@Override
	public void onBindChildViewHolder(DateDetailsChildVH childViewHolder, int position, Object childListItem) {
		try {
			childViewHolder.bind((Present) childListItem);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

class DateDetailsParentVH extends ParentViewHolder {

	TextView level;
	TextView count;

	public DateDetailsParentVH(View itemView) {
		super(itemView);
		level = (TextView) itemView.findViewById(R.id.level);
		count = (TextView) itemView.findViewById(R.id.count);
	}

	public void bind(DateDetailsParent item) {
		level.setText(itemView.getContext().getString(R.string.class_format, item.getLevel()));
		count.setText(String.valueOf(item.getChildItemList().size()));
	}
}

class DateDetailsChildVH extends ChildViewHolder {

	TextView name;

	public DateDetailsChildVH(View itemView) {
		super(itemView);
		name = (TextView) itemView.findViewById(R.id.name);
	}

	public void bind(Present present) {
		name.setText(present.getStudent().getName());
	}
}
