package com.anakarwin.apples.model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by yusuf on 7/24/2017.
 */

public class Payment extends RealmObject {

	public static final String CLASS_NAME = "Payment";
	public static final String FIELD_DATE = "date";
	public static final String FIELD_STUDENT = "student";

	private Date date;
	private Student student;

	public Payment() {
	}

	public Payment(Date date, Student student) {
		this.date = date;
		this.student = student;
	}

	//region getter setter
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
	//endregion
}
