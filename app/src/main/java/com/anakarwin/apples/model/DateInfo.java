package com.anakarwin.apples.model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by E460 on 25/07/2017.
 */

public class DateInfo extends RealmObject {

	public static final String CLASS_NAME = "DateInfo";
	public static final String FIELD_DATE = "date";
	public static final String FIELD_STATUS = "status";

	private Date date;
	private int status;

	public DateInfo() {
	}

	public DateInfo(Date date) {
		this.date = date;
		this.status = Type.toInt(Type.READY);
	}

	public DateInfo(Date date, int status) {
		this.date = date;
		this.status = status;
	}

	public DateInfo(Date date, Type type) {
		this.date = date;
		this.status = Type.toInt(type);
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
