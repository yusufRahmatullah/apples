package com.anakarwin.apples.daily;

import android.support.annotation.NonNull;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.anakarwin.apples.R;
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by E460 on 27/07/2017.
 */

class DailyPresentAdapter extends ExpandableRecyclerAdapter<DailyPresentPVH, DailyPresentCVH> {

	private WeakReference<List<DailyPresentItem>> itemsReference;

	private DailyPresentAdapter(@NonNull List<? extends ParentListItem> parentItemList,
	                            WeakReference<List<DailyPresentItem>> itemsReference) {
		super(parentItemList);
		this.itemsReference = itemsReference;
	}

	static DailyPresentAdapter newInstance(@NonNull List<DailyPresentItem> items) {
		return new DailyPresentAdapter(generateParents(items), new WeakReference<>(items));
	}

	//region override methods
	@Override
	public DailyPresentPVH onCreateParentViewHolder(ViewGroup parentViewGroup) {
		View view = LayoutInflater.from(parentViewGroup.getContext())
			.inflate(R.layout.item_daily_present_parent, parentViewGroup, false);
		return new DailyPresentPVH(view);
	}

	@Override
	public DailyPresentCVH onCreateChildViewHolder(ViewGroup childViewGroup) {
		View view = LayoutInflater.from(childViewGroup.getContext())
			.inflate(R.layout.item_daily_present_child, childViewGroup, false);
		return new DailyPresentCVH(view);
	}

	@Override
	public void onBindParentViewHolder(DailyPresentPVH parentViewHolder, int position, ParentListItem parentListItem) {
		parentViewHolder.bind((DailyPresentParent) parentListItem);
	}

	@Override
	public void onBindChildViewHolder(DailyPresentCVH childViewHolder, int position, Object childListItem) {
		DailyPresentChild child = (DailyPresentChild) childListItem;
		final int parentPos = child.getParentPos();
		final int childPos = child.getChildPos();
		childViewHolder.bind(child, new OnPresentCheckedListener() {
			@Override
			public void onPresentChecked(int position, boolean checked) {
				notifyParentItemChanged(parentPos);
			}
		});
	}
	//endregion

	public void updateDailyPresent() {
		List<DailyPresentItem> dailyPresentItems = itemsReference.get();
		if (dailyPresentItems != null) {
			List<DailyPresentParent> parents = (List<DailyPresentParent>) getParentItemList();
			int currentPos = 0;
			for (int i = 0; i < parents.size(); i++) {
				List<?> children = parents.get(i).getChildItemList();
				for (int j = 0; j < children.size(); j++) {
					DailyPresentChild child = (DailyPresentChild) children.get(j);
					dailyPresentItems.get(currentPos).setPresent(child.getDailyPresent().isPresent());
					currentPos++;
				}
			}
		}
	}

	private static List<DailyPresentParent> generateParents(List<DailyPresentItem> items) {
		// assume items sorted by student level
		List<DailyPresentParent> parents = new ArrayList<>();
		List<Integer> parentPos = new ArrayList<>();
		if (items.size() > 0) {
			int currentLevel = items.get(0).getStudent().getLevel();
			parentPos.add(0);
			for (int i = 1; i < items.size(); i++) {
				int level = items.get(i).getStudent().getLevel();
				if (level != currentLevel) {
					parentPos.add(i);
					currentLevel = level;
				}
			}

			for (int i = 0; i < parentPos.size(); i++) {
				int from = parentPos.get(i);
				int to;
				if (i == parentPos.size() - 1) {
					to = items.size();
				} else {
					to = parentPos.get(i + 1);
				}

				List<DailyPresentChild> children = new ArrayList<>();
				List<DailyPresentItem> itemSubLList = items.subList(from, to);
				for (int j = 0; j < itemSubLList.size(); j++) {
					children.add(new DailyPresentChild(i, j, itemSubLList.get(j)));
				}
				parents.add(new DailyPresentParent(children));
			}
		}
		return parents;
	}
}

class DailyPresentPVH extends ParentViewHolder {

	private TextView level, count;

	public DailyPresentPVH(View itemView) {
		super(itemView);
		level = (TextView) itemView.findViewById(R.id.level);
		count = (TextView) itemView.findViewById(R.id.count);
	}

	public void bind(DailyPresentParent item) {
		level.setText(itemView.getContext().getString(R.string.class_format, item.getLevel()));
		count.setText(String.valueOf(item.getCount()));
	}
}

class DailyPresentCVH extends ChildViewHolder {

	private TextView name;
	private SwitchCompat present;

	public DailyPresentCVH(View itemView) {
		super(itemView);
		name = (TextView) itemView.findViewById(R.id.name);
		present = (SwitchCompat) itemView.findViewById(R.id.present);
	}

	public void bind(final DailyPresentChild item, final OnPresentCheckedListener onPresentCheckedListener) {
		if (item != null && onPresentCheckedListener != null) {
			name.setText(item.getDailyPresent().getStudent().getName());
			present.setOnCheckedChangeListener(null);
			present.setChecked(item.getDailyPresent().isPresent());
			final int actualPos = item.getParentPos() + item.getChildPos();
			present.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
					item.getDailyPresent().setPresent(b);
					onPresentCheckedListener.onPresentChecked(actualPos, b);
				}
			});
		}
	}
}

class DailyPresentParent implements ParentListItem {

	private List<DailyPresentChild> children;

	public DailyPresentParent(List<DailyPresentChild> children) {
		this.children = children;
	}

	@Override
	public List<?> getChildItemList() {
		return children;
	}

	@Override
	public boolean isInitiallyExpanded() {
		return false;
	}

	public int getLevel() {
		if (children != null && children.size() > 0) {
			return children.get(0).getDailyPresent().getStudent().getLevel();
		} else {
			return 0;
		}
	}

	public int getCount() {
		if (children != null) {
			int presentCount = 0;
			for (int i = 0; i < children.size(); i++) {
				if (children.get(i).getDailyPresent().isPresent()) {
					presentCount++;
				}
			}
			return presentCount;
		} else {
			return 0;
		}
	}
}

class DailyPresentChild {

	private int parentPos;
	private int childPos;
	private DailyPresentItem dailyPresent;

	DailyPresentChild(int parentPos, int childPos, DailyPresentItem dailyPresent) {
		this.parentPos = parentPos;
		this.childPos = childPos;
		this.dailyPresent = dailyPresent;
	}

	public int getParentPos() {
		return parentPos;
	}

	public void setParentPos(int parentPos) {
		this.parentPos = parentPos;
	}

	public int getChildPos() {
		return childPos;
	}

	public void setChildPos(int childPos) {
		this.childPos = childPos;
	}

	DailyPresentItem getDailyPresent() {
		return dailyPresent;
	}

	public void setDailyPresent(DailyPresentItem dailyPresent) {
		this.dailyPresent = dailyPresent;
	}
}

interface OnPresentCheckedListener {
	void onPresentChecked(int position, boolean checked);
}