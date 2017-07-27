package com.anakarwin.apples.daily;

import com.anakarwin.apples.model.Student;

/**
 * Created by E460 on 27/07/2017.
 */

public class DailyPresentItem {

	private Student student;
	private boolean present;

	public DailyPresentItem(Student student, boolean present) {
		this.student = student;
		this.present = present;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public boolean isPresent() {
		return present;
	}

	public void setPresent(boolean present) {
		this.present = present;
	}
}
