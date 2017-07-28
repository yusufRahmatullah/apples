package com.anakarwin.apples.model;

import java.util.Calendar;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by E460 on 25/07/2017.
 */

public class DateInfo extends RealmObject {

	public static final String CLASS_NAME = "DateInfo";
	public static final String FIELD_ID = "id";
	public static final String FIELD_DATE = "date";
	public static final String FIELD_STATUS = "status";

	@PrimaryKey
	private int id;
	private Date date;
	private int status;

	public DateInfo() {
		Date date = new Date();
		this.date = date;
		this.id = DAO.getInstance().generateId(date);
		this.status = Type.toInt(Type.READY);
	}

	public DateInfo(Date date) {
		this.id = DAO.getInstance().generateId(date);
		this.date = date;
		this.status = Type.toInt(Type.READY);
	}

	public DateInfo(Date date, Type type) {
		this.date = date;
		this.status = Type.toInt(type);
		this.id = DAO.getInstance().generateId(date);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Type getStatusType() {
		return Type.fromInt(status);
	}

	public void setStatusType(Type type) {
		this.status = Type.toInt(type);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public enum Type {
		HOLIDAY, READY, DONE;

		public static Type fromInt(int type) {
			try {
				return values()[type];
			} catch (Exception e) {
				return READY;
			}
		}

		public static int toInt(Type type) {
			switch (type) {
				case HOLIDAY:
					return 0;
				case READY:
					return 1;
				case DONE:
					return 2;
				default:
					return 1;
			}
		}
	}
}
