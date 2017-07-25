package com.anakarwin.apples.schedule.dateDetails;

import com.anakarwin.apples.model.Present;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

/**
 * Created by E460 on 25/07/2017.
 */

public class DateDetailsParent implements ParentListItem {

	private List<Present> presents;
	private int level;

	public DateDetailsParent(List<Present> presents, int level) {
		this.presents = presents;
		this.level = level;
	}

	@Override
	public List<?> getChildItemList() {
		return presents;
	}

	@Override
	public boolean isInitiallyExpanded() {
		return presents.size() > 0;
	}

	public int getLevel() {
		return level;
	}
}
