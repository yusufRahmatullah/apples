package com.anakarwin.apples.model;

import java.util.Calendar;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by yusuf on 7/24/2017.
 */

public class Payment extends RealmObject {

	public static final String CLASS_NAME = "Payment";
	public static final String FIELD_ID = "id";
	public static final String FIELD_DATE = "date";
	public static final String FIELD_STUDENT = "student";

	@PrimaryKey
	private String id;
	private Date date;
	private Student student;

	public Payment() {
		Date date = new Date();
		this.date = date;
		this.id = DAO.dateFormatter.format(date);
		this.student = null;
	}

	public Payment(Date date, Student student) {
		this.id = DAO.dateFormatter.format(date) + student.getName();
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	//endregion
}
