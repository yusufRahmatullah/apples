package com.anakarwin.apples.student;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anakarwin.apples.OnListItemSelected;
import com.anakarwin.apples.R;
import com.anakarwin.apples.model.Student;
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by E460 on 26/07/2017.
 */

public class StudentAdapter extends ExpandableRecyclerAdapter<StudentParentViewHolder, StudentChildViewHolder> {

	private OnListItemSelected onListItemSelected;

	public StudentAdapter(@NonNull List<? extends ParentListItem> parentItemList) {
		super(parentItemList);
	}

	public static StudentAdapter newInstance(List<Student> students) {
		// assume students is sorted ascending by level
		List<StudentParent> studentParents = new ArrayList<>();
		if (students != null && students.size() > 0) {
			List<Integer> parentPos = new ArrayList<>();
			int currentLevel = students.get(0).getLevel();
			parentPos.add(0);
			for (int i = 1; i < students.size(); i++) {
				int level = students.get(i).getLevel();
				if (level != currentLevel) {
					currentLevel = level;
					parentPos.add(i);
				}
			}
			for (int i = 0; i < parentPos.size(); i++) {
				int from = parentPos.get(i);
				int to;
				if (i == parentPos.size() - 1) {
					to = students.size();
				} else {
					to = parentPos.get(i + 1);
				}
				List<StudentChild> children = new ArrayList<>();
				List<Student> sublist = students.subList(from, to);
				for (Student student : sublist) {
					children.add(new StudentChild(student, from));
				}
				studentParents.add(new StudentParent(children));
			}
		}
		StudentAdapter adapter = new StudentAdapter(studentParents);
		return adapter;
	}

	@Override
	public StudentParentViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
		View view = LayoutInflater.from(parentViewGroup.getContext())
			.inflate(R.layout.item_student_parent, parentViewGroup, false);
		return new StudentParentViewHolder(view);
	}

	@Override
	public StudentChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
		View view = LayoutInflater.from(childViewGroup.getContext())
			.inflate(R.layout.item_student_child, childViewGroup, false);
		return new StudentChildViewHolder(view);
	}

	@Override
	public void onBindParentViewHolder(StudentParentViewHolder parentViewHolder, int position, ParentListItem parent) {
		parentViewHolder.bind((StudentParent) parent);
	}

	@Override
	public void onBindChildViewHolder(StudentChildViewHolder childViewHolder, int position, Object childListItem) {
		StudentChild child = (StudentChild) childListItem;
		final int actualPos = child.getParentPos() + position;
		childViewHolder.bind(child);
		childViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (onListItemSelected != null) {
					onListItemSelected.onItemSelected(actualPos);
				}
			}
		});
	}

	public void setOnListItemSelected(OnListItemSelected onListItemSelected) {
		this.onListItemSelected = onListItemSelected;
	}
}

class StudentParentViewHolder extends ParentViewHolder {

	TextView level, count;

	public StudentParentViewHolder(View itemView) {
		super(itemView);
		level = (TextView) itemView.findViewById(R.id.level);
		count = (TextView) itemView.findViewById(R.id.count);
	}

	public void bind(StudentParent parent) {
		if (parent != null) {
			level.setText(itemView.getContext().getString(R.string.class_format, parent.getLevel()));
			count.setText(String.valueOf(parent.getChildSize()));
		} else {
			level.setText("-");
			count.setText("-");
		}
	}
}

class StudentChildViewHolder extends ChildViewHolder {

	TextView name;

	public StudentChildViewHolder(View itemView) {
		super(itemView);
		name = (TextView) itemView.findViewById(R.id.name);
	}

	public void bind(StudentChild student) {
		if (student != null) {
			name.setText(student.getName());
		} else {
			name.setText("-");
		}
	}
}

class StudentParent implements ParentListItem {

	private List<StudentChild> children;

	public StudentParent(List<StudentChild> children) {
		this.children = children;
	}

	@Override
	public List<?> getChildItemList() {
		return children;
	}

	@Override
	public boolean isInitiallyExpanded() {
		return children != null && children.size() > 0;
	}

	public int getLevel() {
		if (children != null && children.size() > 0) {
			return children.get(0).getLevel();
		}
		return 0;
	}

	public int getChildSize() {
		if (children != null) {
			return children.size();
		} else {
			return 0;
		}
	}
}

class StudentChild {

	private int parentPos;
	private Student student;

	public StudentChild(Student student, int parentPos) {
		this.student = student;
		this.parentPos = parentPos;
	}

	public int getLevel() {
		if (student != null) {
			return student.getLevel();
		} else {
			return 0;
		}
	}

	public String getName() {
		return student != null ? student.getName() : "";
	}

	public int getParentPos() {
		return parentPos;
	}
}