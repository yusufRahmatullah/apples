package com.anakarwin.apples.schedule;

/**
 * Created by yusuf on 7/23/2017.
 */

public class ScheduleItem {

	private int date;
	private Type status;

	public ScheduleItem() {
	}

	public ScheduleItem(int date) {
		this.date = date;
		status = Type.READY;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public Type getStatus() {
		return status;
	}

	public void setStatus(Type status) {
		this.status = status;
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
